package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.match.bean.vo.BaseMatchUsersVo;
import com.baibei.hengjia.api.modules.match.dao.BuymatchLogMapper;
import com.baibei.hengjia.api.modules.match.dao.BuymatchUserMapper;
import com.baibei.hengjia.api.modules.match.model.BuymatchLog;
import com.baibei.hengjia.api.modules.match.model.BuymatchUser;
import com.baibei.hengjia.api.modules.match.model.MatchAuthority;
import com.baibei.hengjia.api.modules.match.model.MatchFailLog;
import com.baibei.hengjia.api.modules.match.service.*;
import com.baibei.hengjia.api.modules.product.model.ProductStock;
import com.baibei.hengjia.api.modules.product.service.IProductStockService;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.dao.HoldDetailsMapper;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.model.*;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
* @author: Longer
* @date: 2019/08/05 11:08:53
* @description: BuymatchLog服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BuymatchLogServiceImpl extends AbstractService<BuymatchLog> implements IBuymatchLogService {

    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private IBuymatchUserService buymatchUserService;
    @Autowired
    private IMatchFailLogService matchFailLogService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private HoldTotalMapper holdTotalMapper;
    @Autowired
    private HoldDetailsMapper holdDetailsMapper;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IProductStockService productStockService;
    @Autowired
    private IMatchAuthorityService matchAuthorityService;
    @Autowired
    private IDeliveryService deliveryService;
    @Autowired
    private BuymatchUserMapper buymatchUserMapper;
    @Autowired
    private BuymatchLogMapper buymatchLogMapper;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private IAsyncService asyncService;
    @Value("${buymatch.expire.num}")
    private int expireNum;//超期次数限制
    @Value("${buymatch.effect.day}")
    private String effectDay;//生效天数
    @Value("${buymatch.fee.rate}")
    private BigDecimal feeRate;//手续费率
    @Value("${buymatch.period.num}")
    private Integer periodNum;//每次只给买入配货多少个用户
    @Value("${buymatch.offset.flag}")
    private String offsetFlag;//是否执行补货逻辑。offset=执行；unoffset=不执行

    @Override
    public ApiResult buyMatch(String batchNo,BaseMatchUsersVo baseMatchUsersVo) {
        long start = System.currentTimeMillis();
        log.info("开始执行单个用户买入配货逻辑....，用户编码为："+baseMatchUsersVo.getCustomerNo());
        if (StringUtils.isEmpty(batchNo)) {
            log.info("配货失败，未指定批次号");
            throw new ServiceException("配货失败，未指定批次号");
        }
        //判断当前时间是否休市状态
        boolean tradeDay = tradeDayService.isTradeDay(new Date());
        if(!tradeDay){
            throw new ServiceException("非交易日不能买入配货");
        }
        //T+5解锁
        TradeDay theFifthTradeDay = tradeDayService.getTheFifthTradeDay();
        /**
         * 基本流程：新增用户持仓（持仓，持仓明细）-->扣减挂牌商持仓-->生成成交单-->扣减用户余额（手续费）-->新增买入配货流水-->记录/更新失败用户信息-->扣减用户配货权
         */
        //查询经销商编码
        String memberCustomerNo = customerService.getDistributorNo();
        if(StringUtils.isEmpty(memberCustomerNo)){
            throw new ServiceException("经销商不存在");
        }
        //执行类型。send=送货；plan=配货
        String operateType=baseMatchUsersVo.getOperateType();
        /*//查看用户配货权信息
        MatchAuthority authority = matchAuthorityService.getAuthority(baseMatchUsersVo.getCustomerNo(), baseMatchUsersVo.getProductTradeNo());
        //提货赋予配货权逻辑
        String deliveryFlag = autoDelivery(authority,baseMatchUsersVo);*/

        //查询用户账户信息
        Account account = accountService.checkAccount(baseMatchUsersVo.getCustomerNo());
        //可用余额
        BigDecimal balance = account.getBalance();
        //查询商品的成本价
        ProductStock productStock = productStockService.findByProductTradeNo(baseMatchUsersVo.getProductTradeNo());
        if(StringUtils.isEmpty(productStock)){
            throw new ServiceException("商品不存在，商品交易编码为："+baseMatchUsersVo.getProductTradeNo());
        }
        //手续费
        BigDecimal fee = new BigDecimal(baseMatchUsersVo.getMatchNum()).multiply(productStock.getCost())
                .multiply(feeRate).setScale(2,BigDecimal.ROUND_DOWN);
        //总成本
        BigDecimal sumCost = new BigDecimal(baseMatchUsersVo.getMatchNum()).multiply(productStock.getCost());
        //总金额
        BigDecimal sumPay = fee.add(sumCost);

        if(balance.compareTo(sumPay)>=0){//足够钱或者新用户<新用户不用扣钱>
            /*if (Constants.BuyMatchOperateType.SEND.equals(operateType)) {
                log.info("送货，用户编码为："+baseMatchUsersVo.getCustomerNo());
            }else{
                log.info("配货，用户资金充足，用户编码为："+baseMatchUsersVo.getCustomerNo()+",资金为："+balance);
            }*/
            log.info("配货，用户资金充足，用户编码为："+baseMatchUsersVo.getCustomerNo()+",资金为："+balance);
            //获取用户持仓信息
            HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(baseMatchUsersVo.getCustomerNo(), baseMatchUsersVo.getProductTradeNo(), Constants.HoldType.MATCH);
            if (StringUtils.isEmpty(holdTotal)) {
                //新增用户持仓
                addHoldTotal(baseMatchUsersVo);
            }else{
                //更新用户商品持仓
                boolean b = holdTotalService.addCustomerProductHoldForMatch(baseMatchUsersVo.getCustomerNo(), baseMatchUsersVo.getProductTradeNo(),
                        new BigDecimal(baseMatchUsersVo.getMatchNum()), Constants.HoldType.MATCH);
                if(!b)
                    throw new ServiceException("配货失败，乐观锁问题，请联系管理员");
            }
            //增加用户持仓明细
            addHoldDetails(baseMatchUsersVo,theFifthTradeDay,productStock.getCost());
            //扣减经销商持仓
           /* boolean b = holdTotalService.deductMemberProductHold(memberCustomerNo, baseMatchUsersVo.getProductTradeNo(), new BigDecimal(baseMatchUsersVo.getMatchNum()), Constants.HoldType.MAIN);
            if(!b){
                throw new ServiceException("扣减经销商总持仓失败，乐观锁问题");
            }*/
            //扣减挂牌商持仓明细
            boolean b1 = holdDetailsService.deductProductHoldByTradeTime(memberCustomerNo, baseMatchUsersVo.getProductTradeNo(), new BigDecimal(baseMatchUsersVo.getMatchNum()), Constants.HoldType.MAIN);
            if(!b1){
                throw new ServiceException("扣减经销商持仓失败，乐观锁问题");
            }
            //生成成交单
            createDealOrder(baseMatchUsersVo,theFifthTradeDay,memberCustomerNo, fee, sumCost);
            //扣减用户余额
            changeAmount(baseMatchUsersVo,sumPay);
            //新增经销商余额
            changeMemberAmount(baseMatchUsersVo.getDealNo(),memberCustomerNo, sumCost,
                    FundTradeTypeEnum.MATCH_SELL.getCode()+"","2");
            //新增配货流水
            addLogs(baseMatchUsersVo,batchNo,memberCustomerNo,productStock.getCost(), fee,
                    baseMatchUsersVo.getFailCount()==0?Constants.BuyMatchLogType.COMMON:Constants.BuyMatchLogType.OFFSET,
                    Constants.Status.SUCCESS);
            if(baseMatchUsersVo.getFailCount()>0){
                //更新失败用户信息
                MatchFailLog matchFailLog = new MatchFailLog();
                matchFailLog.setDealNo(baseMatchUsersVo.getDealNo());
                matchFailLog.setCustomerNo(baseMatchUsersVo.getCustomerNo());
                matchFailLog.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
                matchFailLog.setStatus(Constants.MatchFailLogStatus.DEAL);
                matchFailLogService.modifyFailLog(matchFailLog);
                log.info("补货成功，用户编码为:"+baseMatchUsersVo.getCustomerNo()+"，资金为："+balance);
            }
            //扣减用户配货权(若扣减失败，则直接回滚)
            /*if(Constants.BuyMatchOperateType.PLAN.equals(operateType)){
                int i = matchAuthorityService.detuchAuthority(baseMatchUsersVo.getCustomerNo(), baseMatchUsersVo.getProductTradeNo());
                if(i==0){
                    throw new ServiceException("配货失败，配货权乐观锁问题");
                }
            }*/
        }else{
            log.info("配货用户资金不足，用户编码为:"+baseMatchUsersVo.getCustomerNo()+"，资金为："+balance);
            //记录/更新失败用户信息
            if(baseMatchUsersVo.getFailCount()>0){
                //更新失败用户信息
                MatchFailLog matchFailLog = new MatchFailLog();
                matchFailLog.setDealNo(baseMatchUsersVo.getDealNo());
                matchFailLog.setCustomerNo(baseMatchUsersVo.getCustomerNo());
                matchFailLog.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
                /*if((baseMatchUsersVo.getFailCount()+baseMatchUsersVo.getMatchNum().intValue())>=expireNum){
                    matchFailLog.setStatus(Constants.MatchFailLogStatus.WAIT);
                }
                Date failTime = baseMatchUsersVo.getCreateTime();
                long daySub = DateUtil.getDaySub(failTime, new Date());
                if(daySub>=Long.parseLong(effectDay.trim())){
                    matchFailLog.setStatus(Constants.MatchFailLogStatus.DESTORY);
                }*/
                matchFailLog.setStatus(Constants.MatchFailLogStatus.WAIT);
                matchFailLog.setFailCount(baseMatchUsersVo.getFailCount()+1);
                matchFailLog.setModifyTime(new Date());
                matchFailLogService.modifyFailLog(matchFailLog);
                log.info("配货更新失败表，失败次数为："+matchFailLog.getFailCount());
            }else{
                //插入失败用户信息
                addMatchFailLog(baseMatchUsersVo,Constants.MatchFailLogType.BALANCE_LIMIT);
                log.info("配货插入失败表，失败次数为："+baseMatchUsersVo.getFailCount()+1);
                //异步发送短信通知
                asyncService.buyMatchFailSms(baseMatchUsersVo.getCustomerNo());
            }
        }
        buymatchUserMapper.updateStatusWithRun(batchNo, baseMatchUsersVo.getCustomerNo(),baseMatchUsersVo.getProductTradeNo());
        log.info("buyMatch time comsuming " + (System.currentTimeMillis() - start) + " ms"+",customerNo:"+baseMatchUsersVo.getCustomerNo());
        return ApiResult.success();
    }

    @Override
    public List<BuymatchLog> findByDateAndCustomerAndProductTradeNo(String productTradeNo, String batchNo, String customerNo) {
        Condition condition=new Condition(BuymatchLog.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("batchNo",batchNo);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        criteria.andEqualTo("customerNo",customerNo);
        return buymatchLogMapper.selectByCondition(condition);
    }

    @Override
    public List<BuymatchLog> findBy(String productTradeNo, String batchNo, String customerNo, BigDecimal discountPrice) {
        Condition condition=new Condition(BuymatchLog.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("batchNo",batchNo);
        criteria.andEqualTo("productTradeNo",productTradeNo);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("discountPrice",discountPrice);
        return buymatchLogMapper.selectByCondition(condition);    }

    public String autoDelivery(MatchAuthority authority, BaseMatchUsersVo baseMatchUsersVo){
        String deliveryFlag="success";//提货是否成功标识
        //查询用户的收货地址
        CustomerAddress customerAddress = customerAddressService.findDefaultByNo(baseMatchUsersVo.getCustomerNo());
        if (StringUtils.isEmpty(authority)||authority.getMatchAuthority()==0) {
            if(!StringUtils.isEmpty(customerAddress)){
                //生成提货订单
                DeliveryApplyDto deliveryApplyDto = new DeliveryApplyDto();
                deliveryApplyDto.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
                deliveryApplyDto.setHoldType(Constants.HoldType.MAIN);
                deliveryApplyDto.setDeliveryCount(1);//新商品提货1手
                deliveryApplyDto.setAddressId(customerAddress.getId());
                deliveryApplyDto.setCustomerNo(baseMatchUsersVo.getCustomerNo());
                deliveryApplyDto.setValidateTradeDayFlag("1");
                deliveryApplyDto.setSource(Constants.DeliverySource.SYS);
                ApiResult apiResult = deliveryService.deliveryApply(deliveryApplyDto);
                if(ResultEnum.ERROR.getCode()==apiResult.getCode()){
                    deliveryFlag="fail";
                    log.info("用户买入配货失败，该用户编号为：{}，提货商品为：{}，失败原因为：{}",baseMatchUsersVo.getCustomerNo(),baseMatchUsersVo.getProductTradeNo(),apiResult.getMsg());
                }
            }else{
                log.info("用户买入配货失败，该用户编号为：{}，提货商品为：{}，失败原因为：{}",baseMatchUsersVo.getCustomerNo(),baseMatchUsersVo.getProductTradeNo(),"找不到用户的收货地址");
                deliveryFlag="fail";
            }
        }
        return deliveryFlag;
    }

    public void addLogs(BaseMatchUsersVo baseMatchUsersVo,String batchNo,String memberNo,BigDecimal cost,BigDecimal fee,String type,String status){
        BuymatchLog buymatchLog = new BuymatchLog();
        Date currentDate = new Date();
        buymatchLog.setBatchNo(batchNo);
        buymatchLog.setDealNo(baseMatchUsersVo.getDealNo());
        buymatchLog.setCustomerNo(baseMatchUsersVo.getCustomerNo());
        buymatchLog.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
        buymatchLog.setMemberNo(memberNo);
        buymatchLog.setMatchNum(baseMatchUsersVo.getMatchNum());
        buymatchLog.setDiscountPrice(cost);
        buymatchLog.setFee(fee);
        buymatchLog.setType(type);
        buymatchLog.setStatus(status);
        buymatchLog.setCreateTime(currentDate);
        buymatchLog.setModifyTime(currentDate);
        buymatchLog.setFlag(new Byte(Constants.Flag.VALID));
        this.save(buymatchLog);
    }


    public void addMatchFailLog(BaseMatchUsersVo baseMatchUsersVo,String failType){
        MatchFailLog matchFailLog = new MatchFailLog();
        Date currentDate = new Date();
        matchFailLog.setDealNo(baseMatchUsersVo.getDealNo());
        matchFailLog.setCustomerNo(baseMatchUsersVo.getCustomerNo());
        matchFailLog.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
        matchFailLog.setFailCount(baseMatchUsersVo.getFailCount()+1);
        matchFailLog.setMatchNum(baseMatchUsersVo.getMatchNum());
        matchFailLog.setFailType(failType);
        matchFailLog.setStatus(Constants.MatchFailLogStatus.WAIT);
        matchFailLog.setCreateTime(currentDate);
        matchFailLog.setModifyTime(currentDate);
        matchFailLog.setFlag(new Byte(Constants.Flag.VALID));
        matchFailLogService.save(matchFailLog);
    }



    /**
     * 获取所有配货用户集合
     * @param batchNo
     * @return
     */
    private List<BaseMatchUsersVo> getAllUsers(String batchNo) {
        List<BaseMatchUsersVo> baseMatchUsersVoList = new ArrayList<>();
        //获取以前配货失败用户集合
        if(Constants.OffsetFlag.OFFSET.equals(offsetFlag)){
            List<MatchFailLog> matchFailLogVoList = matchFailLogService.getFailLogsByStatus(Constants.MatchFailLogStatus.WAIT);
            for (MatchFailLog matchFailLog  : matchFailLogVoList) {
                if(baseMatchUsersVoList.size()<periodNum){
                    BaseMatchUsersVo baseMatchUsersVo = new BaseMatchUsersVo();
                    baseMatchUsersVo.setDealNo(matchFailLog.getDealNo());
                    baseMatchUsersVo.setCustomerNo(matchFailLog.getCustomerNo());
                    baseMatchUsersVo.setProductTradeNo(matchFailLog.getProductTradeNo());
                    baseMatchUsersVo.setMatchNum(matchFailLog.getMatchNum());
                    baseMatchUsersVo.setFailCount(matchFailLog.getFailCount());
                    baseMatchUsersVo.setOperateType(Constants.BuyMatchOperateType.PLAN);
                    baseMatchUsersVo.setCreateTime(matchFailLog.getCreateTime());
                    baseMatchUsersVoList.add(baseMatchUsersVo);
                }
            }
        }
        //获取本批次下的用户集合
        List<BuymatchUser> buymatchUserList = buymatchUserService.getByBatchNo(batchNo,Constants.BuyMatchUserStatus.UNRUN);
        //合并集合
        for (BuymatchUser buymatchUser : buymatchUserList) {
            if(baseMatchUsersVoList.size()<periodNum) {
                BaseMatchUsersVo baseMatchUsersVo = new BaseMatchUsersVo();
                baseMatchUsersVo.setDealNo(buymatchUser.getDealNo());
                baseMatchUsersVo.setCustomerNo(buymatchUser.getCustomerNo());
                baseMatchUsersVo.setProductTradeNo(buymatchUser.getProductTradeNo());
                baseMatchUsersVo.setMatchNum(buymatchUser.getMatchNum());
                baseMatchUsersVo.setFailCount(0);
                baseMatchUsersVo.setOperateType(buymatchUser.getOperateType());
                baseMatchUsersVo.setCreateTime(buymatchUser.getCreateTime());
                baseMatchUsersVoList.add(baseMatchUsersVo);
            }
        }
        return baseMatchUsersVoList;
    }

    /**
     * 新增用户持仓
     * @param baseMatchUsersVo
     */
    public void addHoldTotal(BaseMatchUsersVo baseMatchUsersVo){
        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setCustomerNo(baseMatchUsersVo.getCustomerNo());
        newHoldTotal.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
        newHoldTotal.setTotalCount(new BigDecimal(baseMatchUsersVo.getMatchNum()));
        newHoldTotal.setFrozenCount(new BigDecimal(0));
        newHoldTotal.setCanSellCount(new BigDecimal(0));
        newHoldTotal.setCost(new BigDecimal(0));
        newHoldTotal.setHoldType(Constants.HoldType.MATCH);
        newHoldTotal.setCreateTime(new Date());
        newHoldTotal.setModifyTime(new Date());
        newHoldTotal.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdTotalMapper.insert(newHoldTotal);
    }

    /**
     * 新增持仓明细
     */
    public void addHoldDetails(BaseMatchUsersVo baseMatchUsersVo,TradeDay theFifthTradeDay,BigDecimal cost){
        HoldDetails holdDetails = new HoldDetails();
        holdDetails.setCustomerNo(baseMatchUsersVo.getCustomerNo());
        holdDetails.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
        holdDetails.setOriginalCount(new BigDecimal(baseMatchUsersVo.getMatchNum()));
        holdDetails.setFrozenCount(new BigDecimal(0));
        holdDetails.setRemaindCount(new BigDecimal(baseMatchUsersVo.getMatchNum()));
        holdDetails.setCost(cost);
        holdDetails.setHoldTime(new Date());
        holdDetails.setHoldNo(baseMatchUsersVo.getDealNo());
        holdDetails.setResource(Constants.HoldResource.PLAN);
        holdDetails.setScanner(Byte.valueOf("0"));
        holdDetails.setHoldType(Constants.HoldType.MATCH);
        holdDetails.setCreateTime(new Date());
        holdDetails.setModifyTime(new Date());
        holdDetails.setTradeTime(theFifthTradeDay.getTradeDay());//交易日期
        holdDetails.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdDetailsMapper.insert(holdDetails);
    }

    private void createDealOrder(BaseMatchUsersVo baseMatchUsersVo,TradeDay theFifthTradeDay,String memberCustomerNo,BigDecimal fee,BigDecimal cost) {
        DealOrder dealOrder = new DealOrder();
        dealOrder.setDealNo(baseMatchUsersVo.getDealNo());
        dealOrder.setEntrustId(0L);
        dealOrder.setProductTradeNo(baseMatchUsersVo.getProductTradeNo());
        dealOrder.setBuyCustomerNo(baseMatchUsersVo.getCustomerNo());
        dealOrder.setSellCustomerNo(memberCustomerNo);//挂牌商
        dealOrder.setBuyFee(fee);
        dealOrder.setType(Constants.TradeDirection.BUY);
        dealOrder.setPrice(cost);
        dealOrder.setCount(baseMatchUsersVo.getMatchNum().intValue());
        dealOrder.setHoldType(Constants.HoldType.MATCH);
        dealOrderService.save(dealOrder);
    }


    /**
     * 更新账户余额
     * @param baseMatchUsersVo
     */
    public void changeAmount(BaseMatchUsersVo baseMatchUsersVo,BigDecimal sumPay){
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(baseMatchUsersVo.getCustomerNo());
        changeAmountDto.setChangeAmount(sumPay);
        changeAmountDto.setOrderNo(baseMatchUsersVo.getDealNo());
        changeAmountDto.setTradeType(Byte.valueOf(FundTradeTypeEnum.MATCH_BUY.getCode()+""));
        changeAmountDto.setReType(Byte.valueOf("1"));
        accountService.changeAccount(changeAmountDto);
    }

    /**
     * 更新挂牌商余额
     */
    public void changeMemberAmount(String dealNo,String memberCustomerNo,BigDecimal changeAmount,String tradeType,String reType){
        //查询挂牌商编码
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(memberCustomerNo);
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setOrderNo(dealNo);
        changeAmountDto.setTradeType(Byte.valueOf(tradeType));
        changeAmountDto.setReType(Byte.valueOf(reType));
        accountService.changeAccount(changeAmountDto);
    }
}
