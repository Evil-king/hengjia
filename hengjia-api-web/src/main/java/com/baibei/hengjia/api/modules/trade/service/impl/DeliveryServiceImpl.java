package com.baibei.hengjia.api.modules.trade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.match.service.IMatchAuthorityService;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryQueryDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryTransferDto;
import com.baibei.hengjia.api.modules.trade.dao.DeliveryDetailsMapper;
import com.baibei.hengjia.api.modules.trade.dao.DeliveryMapper;
import com.baibei.hengjia.api.modules.trade.dao.HoldDetailsMapper;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.model.Delivery;
import com.baibei.hengjia.api.modules.trade.model.DeliveryDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.service.IDeliveryService;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryVo;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * @author: Longer
 * @date: 2019/06/05 10:46:05
 * @description: Delivery服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class DeliveryServiceImpl extends AbstractService<Delivery> implements IDeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;
    @Autowired
    private DeliveryDetailsMapper deliveryDetailsMapper;
    @Autowired
    private HoldTotalMapper holdTotalMapper;
    @Autowired
    private HoldDetailsMapper holdDetailsMapper;
    @Autowired
    private IProductMarketService productMarketService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private IMatchAuthorityService matchAuthorityService;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICouponAccountService couponAccountService;

    @Value("${delivery.count}")
    private Integer DELIVERY_COUNT;//提货最小数量
    @Value("${match.delivery.flag}")
    private boolean delveryFlag;//新折扣商品是否能提货标识
    @Value("${newproduct.list}")
    private String newProductList;//新商品编码集合,逗号分隔

    @Value("${receiptDay}")
    private Long receiptDay; //收货天数

    /**
     * 用户提货申请
     * {
     * 2019-06-14 19:42  需求更改：为了快速上线，现在改成，用户提货，则提货订单状态直接改成 “待发货”状态，并且直接扣减持仓量。
     * }
     *
     * @param deliveryApplyDto
     */
    @Override
    public ApiResult deliveryApply(DeliveryApplyDto deliveryApplyDto) {
        ApiResult apiResult = new ApiResult();
        boolean tradeDay = tradeDayService.isTradeTime();
        if ("0".equals(deliveryApplyDto.getValidateTradeDayFlag()) && !tradeDay) {
            return ApiResult.error("休市时间不能提货");
        }

        if (deliveryApplyDto.getCustomerNo() == null || " ".equals(deliveryApplyDto.getCustomerNo())) {
            return ApiResult.error("提货失败，未指定用户");
        }

        //查询客户持仓
        List<HoldTotal> myHold = getMyHold(deliveryApplyDto);
        if (myHold == null || myHold.size() == 0) {
            return ApiResult.error("无此持仓商品");
        }
        HoldTotal holdTotal = myHold.get(0);
        //可提数量=总量-冻结量
        if (deliveryApplyDto.getDeliveryCount().intValue() > (holdTotal.getTotalCount().intValue() - holdTotal.getFrozenCount().intValue())) {
            return ApiResult.error("可提数量不足");
        }
        if (deliveryApplyDto.getDeliveryCount().intValue() == 0) {
            return ApiResult.error("未指定提货数量");
        }
        //根据productTradeNo查询商品信息
        ProductMarket productMarket = productMarketService.findByProductTradeNo(deliveryApplyDto.getProductTradeNo());
        if (productMarket == null) {
            return ApiResult.error("查无此提货商品");
        }

        if (StringUtils.isEmpty(productMarket.getDeliveryNum())) {
            return ApiResult.error("未指定最小提货数量");
        }

        if (productMarket.getDeliveryNum() > deliveryApplyDto.getDeliveryCount()) {
            return ApiResult.error("提货数量不能小于" + productMarket.getDeliveryNum());
        }
        if (deliveryApplyDto.getDeliveryCount() % productMarket.getDeliveryNum() != 0) {
            return ApiResult.error("提货数量必须是" + productMarket.getDeliveryNum() + "的整数倍");
        }

        String[] newProductArr = newProductList.split(",");
        boolean isNewProduct = false;
        for (String productTradeNo : newProductArr) {
            if (productTradeNo.equals(deliveryApplyDto.getProductTradeNo())) {
                isNewProduct = true;
            }
        }
        //校验该商品是否可以提货
        for (String productTradeNo : newProductArr) {
            if (productTradeNo.equals(productMarket.getProductTradeNo()) && holdTotal.getHoldType().equals(Constants.HoldType.MATCH) && !delveryFlag) {
                return ApiResult.error("新折扣商品不允许提货");
            }
        }
        deliveryApplyDto.setProductId(productMarket.getId());
        //指定提货明细
        BigDecimal count = new BigDecimal(0);
        List<HoldDetails> myHoldDetails = getMyHoldDetails(deliveryApplyDto);
        List<Map> deliveryList = new ArrayList<>();
        int deliveryCount = deliveryApplyDto.getDeliveryCount().intValue();//提货数量
        String s = deliveryList(deliveryList, deliveryCount, myHoldDetails, holdTotal, count);//获取提货明细
        if (s.equals("fail")) {
            return ApiResult.error("提货失败,可提数量不足");
        }
        /*holdTotal.setFrozenCount(new BigDecimal(holdTotal.getFrozenCount().intValue()+deliveryCount));//商品冻结数量增加*/
        //减少持仓数量
        holdTotal.setTotalCount(holdTotal.getTotalCount().subtract(new BigDecimal(deliveryCount)));
        Date currentDate = new Date();
        holdTotal.setModifyTime(currentDate);
        //更新持仓商品
        holdTotalMapper.updateByPrimaryKey(holdTotal);
        //新增一条提货订单
        Delivery delivery = new Delivery();
        delivery.setDeliveryNo(NumberUtil.getRandomID());//生成提货订单号
        delivery.setCustomerNo(deliveryApplyDto.getCustomerNo());
        delivery.setProductId(deliveryApplyDto.getProductId());
        delivery.setAddressId(deliveryApplyDto.getAddressId());
        delivery.setProductTradeNo(productMarket.getProductTradeNo());
        delivery.setProductTradeName(productMarket.getProductTradeName());
        delivery.setIssuePrice(productMarket.getIssuePrice());//商品发行价
        delivery.setCreateTime(currentDate);
        delivery.setModifyTime(currentDate);
        delivery.setDeliveryTime(currentDate);//申请时间
        delivery.setDeliveryStatus(Integer.parseInt(Constants.DeliveryStatus.UNSEND));//待审核状态
        delivery.setDeliveryCount(deliveryApplyDto.getDeliveryCount());//提货数量
        if (Constants.HoldResource.EXCHANGE.equals(deliveryApplyDto.getHoldType())) {
            delivery.setHoldType(Constants.HoldType.MATCH);//商品类型（本票/配票）
        }else{
            delivery.setHoldType(deliveryApplyDto.getHoldType());//商品类型（本票/配票）
        }
        delivery.setRemark(deliveryApplyDto.getRemark());//备注
        delivery.setFlag(Byte.valueOf(Constants.Flag.VALID));
        delivery.setAuditTime(new Date());
        if(Constants.HoldResource.EXCHANGE.equals(deliveryApplyDto.getHoldType())){
            delivery.setSource(Constants.HoldResource.EXCHANGE);
        }else{
            delivery.setSource(deliveryApplyDto.getSource());
        }
        deliveryMapper.insert(delivery);
        //更新持仓明细和插入提货明细
        for (Map holdMap : deliveryList) {
            HoldDetails holdDetail = (HoldDetails) holdMap.get("holdDetail");
            holdDetail.setModifyTime(currentDate);
            holdDetailsMapper.updateByPrimaryKey(holdDetail);//更新持仓明细
            //插入提货明细
            DeliveryDetails deliveryDetail = new DeliveryDetails();
            deliveryDetail.setDeliveryId(delivery.getId());
            deliveryDetail.setDeliveryCount(new BigDecimal(holdMap.get("deliveryCount").toString()).intValue());
            deliveryDetail.setHoldId(holdDetail.getId());
            deliveryDetail.setCreateTime(currentDate);
            deliveryDetail.setModifyTime(currentDate);
            deliveryDetail.setFlag(Byte.valueOf(Constants.Flag.VALID));
            deliveryDetailsMapper.insert(deliveryDetail);
        }
        /*//买入配货规则，提货赋予配货权fdsfds
        if (Constants.MatchRule.BUY_MATCH.equals(productMarket.getMatchRule())) {
            matchAuthorityService.addOrRefleshAuthority(deliveryApplyDto.getCustomerNo(), deliveryApplyDto.getProductTradeNo());
        }*/
        return apiResult;
    }

    /**
     * 主要流程：扣减用户提货券余额-->扣减挂牌商持仓-->生成用户提货单
     * @param deliveryTransferDto
     * @return
     */
    @Override
    public ApiResult deliveryTransfer(DeliveryTransferDto deliveryTransferDto) {
        boolean tradeDay = tradeDayService.isTradeDay(new Date());
        if(!tradeDay){
            return ApiResult.error("非交易日，不能进行提货券提货");
        }
        ApiResult apiResult = new ApiResult();
        if (deliveryTransferDto.getCustomerNo() == null || " ".equals(deliveryTransferDto.getCustomerNo())) {
            return ApiResult.error("提货失败，未指定用户");
        }
        //查询用户的收货地址
        CustomerAddress customerAddress = customerAddressService.findDefaultByNo(deliveryTransferDto.getCustomerNo());
        //查询挂牌商编码
        String memberCustomerNo = customerService.getMemberCustomerNoByProductNo(deliveryTransferDto.getProductTradeNo());

        //查询客户持仓
        List<HoldTotal> myHold = getMyHold(memberCustomerNo,deliveryTransferDto.getProductTradeNo(),Constants.HoldType.MAIN);
        if (myHold == null || myHold.size() == 0) {
            return ApiResult.error("无此持仓商品");
        }
        HoldTotal holdTotal = myHold.get(0);

        BigDecimal orgTotalCount=holdTotal.getTotalCount();
        BigDecimal orgCanSellCount = holdTotal.getCanSellCount();
        BigDecimal orgFrozenCount = holdTotal.getFrozenCount();
        //用户提货券余额
        ApiResult<BigDecimal> deliveryTicketData = couponAccountService.getByCustomerNo(deliveryTransferDto.getCustomerNo(),deliveryTransferDto.getProductTradeNo(), Constants.CouponType.DELIVERYTICKET);

        //获取商品发行价
        ProductMarket productMarket = productMarketService.findByProductTradeNo(deliveryTransferDto.getProductTradeNo());
        if (productMarket == null) {
            return ApiResult.error("查无此提货商品");
        }
        BigDecimal deliveryTicket = deliveryTicketData.getData();
        //提货数量
        int deliveryCount = deliveryTicket.divide(productMarket.getIssuePrice(),2,BigDecimal.ROUND_DOWN).intValue();
        if(deliveryCount<1){
            return ApiResult.error("提货券余额不足");
        }

        //可提数量=总量-冻结量
        if (deliveryCount > (holdTotal.getTotalCount().intValue() - holdTotal.getFrozenCount().intValue())) {
            return ApiResult.error("可提数量不足");
        }
        String deliveryNo=NumberUtil.getRandomID();

        //扣减用户提货券
        ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
        changeCouponAccountDto.setCustomerNo(deliveryTransferDto.getCustomerNo());
        changeCouponAccountDto.setProductTradeNo(deliveryTransferDto.getProductTradeNo());
        changeCouponAccountDto.setCouponType(Constants.CouponType.DELIVERYTICKET);
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.DELIVERY_TICKET_CONSUMPTION.getCode());
        changeCouponAccountDto.setReType(new Byte("1"));
        changeCouponAccountDto.setOrderNo(deliveryNo);
        changeCouponAccountDto.setChangeAmount(new BigDecimal(deliveryCount).multiply(productMarket.getIssuePrice()));
        couponAccountService.changeAmount(changeCouponAccountDto);

        //指定提货明细
        BigDecimal count = new BigDecimal(0);
        Map<String,String> orignDetailsMap = new HashMap();
        List<HoldDetails> myHoldDetails = getMyHoldDetails(memberCustomerNo,deliveryTransferDto.getProductTradeNo(),Constants.HoldType.MAIN);
        for (HoldDetails myHoldDetail : myHoldDetails) {
            orignDetailsMap.put(myHoldDetail.getId()+"",myHoldDetail.getRemaindCount()+"");
        }
        List<Map> deliveryList = new ArrayList<>();
        String s = deliveryList(deliveryList, deliveryCount, myHoldDetails, holdTotal, count);//获取提货明细
        if (s.equals("fail")) {
            return ApiResult.error("提货失败,可提数量不足");
        }
        /*holdTotal.setFrozenCount(new BigDecimal(holdTotal.getFrozenCount().intValue()+deliveryCount));//商品冻结数量增加*/
        //减少持仓数量
        Condition cd = new Condition(HoldTotal.class);
        Example.Criteria ct = cd.createCriteria();
        ct.andEqualTo("id",holdTotal.getId());
        ct.andEqualTo("totalCount",orgTotalCount);
        ct.andEqualTo("canSellCount",orgCanSellCount);
        ct.andEqualTo("frozenCount",orgFrozenCount);
        holdTotal.setTotalCount(holdTotal.getTotalCount().subtract(new BigDecimal(deliveryCount)));
        Date currentDate = new Date();
        holdTotal.setModifyTime(currentDate);
        //更新持仓商品
        int j = holdTotalMapper.updateByConditionSelective(holdTotal, cd);
        if(j==0){
            return ApiResult.error("提货失败，总持仓乐观锁问题");
        }
        /* holdTotalMapper.updateByPrimaryKey(holdTotal);*/
        //新增一条提货订单
        Delivery delivery = new Delivery();
        delivery.setDeliveryNo(deliveryNo);//生成提货订单号
        delivery.setCustomerNo(deliveryTransferDto.getCustomerNo());
        delivery.setProductId(productMarket.getId());
        delivery.setAddressId(customerAddress.getId());
        delivery.setProductTradeNo(productMarket.getProductTradeNo());
        delivery.setProductTradeName(productMarket.getProductTradeName());
        delivery.setIssuePrice(productMarket.getIssuePrice());//商品发行价
        delivery.setCreateTime(currentDate);
        delivery.setModifyTime(currentDate);
        delivery.setDeliveryTime(currentDate);//申请时间
        delivery.setDeliveryStatus(Integer.parseInt(Constants.DeliveryStatus.UNSEND));//待审核状态
        delivery.setDeliveryCount(deliveryCount);//提货数量
        delivery.setHoldType(Constants.HoldType.MAIN);//商品类型（本票/配票）
        delivery.setSource(Constants.DeliverySource.DELIVERYTICKET);
        delivery.setFlag(Byte.valueOf(Constants.Flag.VALID));
        delivery.setAuditTime(currentDate);
        deliveryMapper.insert(delivery);
        //更新持仓明细和插入提货明细
        for (Map holdMap : deliveryList) {
            Condition condition = new Condition(HoldDetails.class);
            Example.Criteria criteria = condition.createCriteria();
            HoldDetails holdDetail = (HoldDetails) holdMap.get("holdDetail");
            String orignRemaindCount = orignDetailsMap.get(holdDetail.getId() + "");
            holdDetail.setModifyTime(currentDate);
            criteria.andEqualTo("remaindCount",orignRemaindCount);//乐观锁
            criteria.andEqualTo("id",holdDetail.getId());
            int i = holdDetailsMapper.updateByConditionSelective(holdDetail, condition);//更新持仓明细
            if(i==0){
                return ApiResult.error("提货失败,持仓明细，乐观锁问题");
            }
            //插入提货明细
            DeliveryDetails deliveryDetail = new DeliveryDetails();
            deliveryDetail.setDeliveryId(delivery.getId());
            deliveryDetail.setDeliveryCount(new BigDecimal(holdMap.get("deliveryCount").toString()).intValue());
            deliveryDetail.setHoldId(holdDetail.getId());
            deliveryDetail.setCreateTime(currentDate);
            deliveryDetail.setModifyTime(currentDate);
            deliveryDetail.setFlag(Byte.valueOf(Constants.Flag.VALID));
            deliveryDetailsMapper.insert(deliveryDetail);
        }
        return apiResult;
    }

    @Override
    public MyPageInfo<MyDeliveryVo> myDeliveryList(DeliveryQueryDto queryDto) {
        List<MyDeliveryVo> myDeliveryVoList = null;
        //时间戳格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (queryDto.getStartTime() != null && !"".equals(queryDto.getStartTime())) {
            long startLong = new Long(queryDto.getStartTime());
            Date startTime = new Date(startLong);
            queryDto.setStartTime(simpleDateFormat.format(startTime));
        }
        if (queryDto.getEndTime() != null && !"".equals(queryDto.getEndTime())) {
            long endLong = new Long(queryDto.getEndTime());
            Date endTime = new Date(endLong);
            queryDto.setEndTime(simpleDateFormat.format(endTime));
        }
        PageHelper.startPage(queryDto.getCurrentPage(), queryDto.getPageSize());
        myDeliveryVoList = deliveryMapper.selectForPage(queryDto);
        return new MyPageInfo<>(myDeliveryVoList);
    }

    @Override
    public Delivery getDeliveryByProAndCust(String customerNo, String productTradeNo) {
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("productTradeNo", productTradeNo);
        List<Delivery> deliveries = deliveryMapper.selectByCondition(condition);
        return deliveries.size() == 0 ? null : deliveries.get(0);
    }

    @Override
    public Delivery getTheFirstDelivery(String customerNo) {
        return deliveryMapper.selectTheFirstDelivery(customerNo);
    }


    @Override
    public void receipt(DeliveryApplyDto deliveryApplyDto) {
        log.info("当前收货的单号为{}", JSONObject.toJSONString(deliveryApplyDto));
        Condition condition = new Condition(Delivery.class);
        condition.createCriteria().andEqualTo("deliveryNo", deliveryApplyDto.getDeliveryNo());
        Delivery delivery = this.findOneByCondition(condition);
        if (delivery.getDeliveryStatus().equals(Integer.valueOf(Constants.DeliveryStatus.RECEIVED))) {
            throw new ServiceException("订单已经收货,不需要重复收货");
        }
        Optional.ofNullable(delivery).map(x -> {
            x.setDeliveryStatus(Integer.valueOf(Constants.DeliveryStatus.RECEIVED)); //已收货
            x.setReceiveTime(new Date()); //收货时间
            x.setModifyTime(new Date());
            this.update(x);
            return x;
        }).orElse(null);
    }


    @Override
    public void autoReceipt() {
        Condition condition = new Condition(Delivery.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deliveryStatus", Constants.DeliveryStatus.SENT);
        List<Delivery> deliveries = this.findByCondition(condition);
        //获取时间相差15天
        // 2019-07-13+15>=  15天自动收货, 2019-07-14
        deliveries.stream().forEach(result -> {
            if (isReceiptDate(result)) {
                result.setDeliveryStatus(Integer.valueOf(Constants.DeliveryStatus.RECEIVED));
                result.setReceiveTime(new Date()); //收货时间
                result.setModifyTime(new Date());
                this.update(result);
            }
        });
    }

    // 1、如果我是2016-07-08 某段时间发货的,那2016-09-15 自动收货
    public Boolean isReceiptDate(Delivery delivery) {
        Instant instant = delivery.getPendingTime().toInstant();
        ZoneId zone = ZoneId.of("Asia/Shanghai");
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate nowDate = nowDateTime.toLocalDate();
        Boolean result = nowDate.plusDays(receiptDay).compareTo(LocalDate.now()) <= 0;
        log.info("当前收货单号为{},发货日期时间为{},比较结果为{}",delivery.getDeliveryNo(),nowDate, result);
        if (result) {
            return true;
        }
        return false;
    }


    public String deliveryList(List<Map> deliveryList, int deliveryCount, List<HoldDetails> myHoldDetails, HoldTotal holdTotal, BigDecimal count) {
        String result = "success";
        BigDecimal sumHold = new BigDecimal("0");
        for (HoldDetails myHoldDetail : myHoldDetails) {
            sumHold = sumHold.add(myHoldDetail.getRemaindCount());
            Map map = new HashMap();
            if (count.compareTo(new BigDecimal(deliveryCount)) != 0) {
                BigDecimal remainCount = myHoldDetail.getRemaindCount();//商品剩余数量
                count = remainCount.add(count);
                if (count.compareTo(new BigDecimal(deliveryCount)) <= 0) {
                    myHoldDetail.setRemaindCount(remainCount.subtract(remainCount));//剩余商品数量较少
                    /*myHoldDetail.setFrozenCount(myHoldDetail.getFrozenCount().add(remainCount));//冻结商品数量增加*/
                    myHoldDetail.setOriginalCount(myHoldDetail.getOriginalCount().subtract(remainCount));//减少原始数量
                    map.put("deliveryCount", remainCount);//提货数量
                    if (myHoldDetail.getScanner().intValue() == Integer.parseInt(Constants.HoldScaner.SCANED))//可卖商品
                        holdTotal.setCanSellCount(holdTotal.getCanSellCount().subtract(remainCount));
                } else {
                    BigDecimal lastCount = new BigDecimal(deliveryCount).subtract((count.subtract(remainCount)));
                    count = new BigDecimal(deliveryCount);
                    myHoldDetail.setRemaindCount(remainCount.subtract(lastCount));//剩余商品数量较少
                    /*myHoldDetail.setFrozenCount(myHoldDetail.getFrozenCount().add(lastCount));//冻结商品数量增加*/
                    myHoldDetail.setOriginalCount(myHoldDetail.getOriginalCount().subtract(lastCount));//减少原始数量
                    map.put("deliveryCount", lastCount);//提货数量
                    if (myHoldDetail.getScanner().intValue() == Integer.parseInt(Constants.HoldScaner.SCANED))//可卖商品
                        holdTotal.setCanSellCount(holdTotal.getCanSellCount().subtract(lastCount));
                }
                map.put("holdDetail", myHoldDetail);
                deliveryList.add(map);
            }
        }
        if (deliveryCount > sumHold.intValue())
            result = "fail";
        /*throw new ServiceException("提货失败,可提数量不足");*/
        return result;
    }

    /**
     * 查询客户某个商品持仓信息
     *
     * @param deliveryApplyDto
     * @return
     */
    public List<HoldTotal> getMyHold(DeliveryApplyDto deliveryApplyDto) {
        //查询客户持仓
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(deliveryApplyDto.getCustomerNo())) {
            criteria.andEqualTo("customerNo", deliveryApplyDto.getCustomerNo());
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getProductTradeNo())) {
            criteria.andEqualTo("productTradeNo", deliveryApplyDto.getProductTradeNo());
        }
        if (Constants.HoldResource.EXCHANGE.equals(deliveryApplyDto.getHoldType())) {
            criteria.andEqualTo("holdType", Constants.HoldType.MATCH);
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getHoldType())&&
                !Constants.HoldResource.EXCHANGE.equals(deliveryApplyDto.getHoldType())) {
            criteria.andEqualTo("holdType", deliveryApplyDto.getHoldType());
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getFlag())) {
            criteria.andEqualTo("flag", deliveryApplyDto.getFlag());
        }
        List<HoldTotal> holdTotals = holdTotalMapper.selectByCondition(condition);
        return holdTotals;
    }

    /**
     * 查询客户某个商品持仓明细
     *
     * @param deliveryApplyDto
     * @return
     */
    public List<HoldDetails> getMyHoldDetails(DeliveryApplyDto deliveryApplyDto) {
        Condition condition = new Condition(HoldDetails.class);
        condition.orderBy("tradeTime").desc();
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(deliveryApplyDto.getCustomerNo())) {
            criteria.andEqualTo("customerNo", deliveryApplyDto.getCustomerNo());
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getProductTradeNo())) {
            criteria.andEqualTo("productTradeNo", deliveryApplyDto.getProductTradeNo());
        }

        if (Constants.HoldResource.EXCHANGE.equals(deliveryApplyDto.getHoldType())) {
            criteria.andEqualTo("holdType", Constants.HoldType.MATCH);
            criteria.andEqualTo("resource", Constants.HoldResource.EXCHANGE);
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getHoldType())&&
                !Constants.HoldResource.EXCHANGE.equals(deliveryApplyDto.getHoldType())) {
            criteria.andEqualTo("holdType", deliveryApplyDto.getHoldType());
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getHoldType())) {
            criteria.andGreaterThan("remaindCount", 0);
        }
        if (!StringUtils.isEmpty(deliveryApplyDto.getFlag())) {
            criteria.andEqualTo("flag", deliveryApplyDto.getFlag());
        }
        List<HoldDetails> holdDetails = holdDetailsMapper.selectByCondition(condition);
        return holdDetails;
    }

    /**
     * 查询客户某个商品持仓明细
     *
     * @param deliveryApplyDto
     * @return
     */
    public List<HoldDetails> getMyHoldDetails(String customerNo,String productTradeNo,String holdType) {
        Condition condition = new Condition(HoldDetails.class);
        condition.orderBy("tradeTime").desc();
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(customerNo)) {
            criteria.andEqualTo("customerNo", customerNo);
        }
        if (!StringUtils.isEmpty(productTradeNo)) {
            criteria.andEqualTo("productTradeNo", productTradeNo);
        }
        if (!StringUtils.isEmpty(holdType)) {
            criteria.andEqualTo("holdType", holdType);
        }

        criteria.andGreaterThan("remaindCount", 0);
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<HoldDetails> holdDetails = holdDetailsMapper.selectByCondition(condition);
        return holdDetails;
    }

    /**
     * 查询客户某个商品持仓信息
     *
     * @param deliveryApplyDto
     * @return
     */
    public List<HoldTotal> getMyHold(String customerNo,String productTradeNo,String holdType) {
        //查询客户持仓
        Condition condition = new Condition(HoldTotal.class);
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(customerNo)) {
            criteria.andEqualTo("customerNo", customerNo);
        }
        if (!StringUtils.isEmpty(productTradeNo)) {
            criteria.andEqualTo("productTradeNo", productTradeNo);
        }
        if (!StringUtils.isEmpty(holdType)) {
            criteria.andEqualTo("holdType", holdType);
        }
        criteria.andEqualTo("flag", Constants.Flag.VALID);
        List<HoldTotal> holdTotals = holdTotalMapper.selectByCondition(condition);
        return holdTotals;
    }

    @Override
    public BigDecimal sumAmount(Date beginTime, Date endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("beginTime", beginTime);
        params.put("endTime", endTime);
        return deliveryMapper.sumAmount(params);
    }

      public static void main(String[] args) {
          int deliveryCount = new BigDecimal("29").divide(new BigDecimal("46.4"),2,BigDecimal.ROUND_DOWN).intValue();
          System.out.println(deliveryCount);
      }

}
