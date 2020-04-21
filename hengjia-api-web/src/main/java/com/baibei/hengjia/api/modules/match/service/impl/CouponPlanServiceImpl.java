package com.baibei.hengjia.api.modules.match.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeCouponAccountDto;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.model.CouponAccount;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.match.bean.dto.CouponPlanDto;
import com.baibei.hengjia.api.modules.match.bean.dto.CouponPlanExecuteDto;
import com.baibei.hengjia.api.modules.match.bean.dto.MatchPlanDetailDto;
import com.baibei.hengjia.api.modules.match.bean.dto.OffsetCouponDto;
import com.baibei.hengjia.api.modules.match.bean.vo.CouponPlanVo;
import com.baibei.hengjia.api.modules.match.dao.CouponPlanMapper;
import com.baibei.hengjia.api.modules.match.model.CouponOffset;
import com.baibei.hengjia.api.modules.match.model.CouponPlan;
import com.baibei.hengjia.api.modules.match.model.MatchPlanDetail;
import com.baibei.hengjia.api.modules.match.service.ICouponOffsetService;
import com.baibei.hengjia.api.modules.match.service.ICouponPlanService;
import com.baibei.hengjia.api.modules.match.service.IMatchPlanDetailService;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;
import com.baibei.hengjia.api.modules.trade.dao.HoldDetailsMapper;
import com.baibei.hengjia.api.modules.trade.dao.HoldTotalMapper;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.api.modules.trade.model.TradeDay;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.api.modules.trade.service.IHoldDetailsService;
import com.baibei.hengjia.api.modules.trade.service.IHoldTotalService;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.model.Customer;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.CouponAccountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/08/07 17:57:17
* @description: CouponPlan服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class CouponPlanServiceImpl extends AbstractService<CouponPlan> implements ICouponPlanService {

    @Autowired
    private CouponPlanMapper tblCpCouponPlanMapper;

    @Autowired
    private IMatchPlanDetailService matchPlanDetailService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICouponAccountService couponAccountService;

    @Autowired
    private IProductMarketService productMarketService;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IHoldTotalService holdTotalService;

    @Autowired
    private HoldTotalMapper holdTotalMapper;

    @Autowired
    private HoldDetailsMapper holdDetailsMapper;

    @Autowired
    private IHoldDetailsService holdDetailsService;

    @Autowired
    private IDealOrderService dealOrderService;

    @Autowired
    private ICouponOffsetService couponOffsetService;

    //抵扣开关 on=开启 off=关闭
    @Value("${coupon.offset.switch}")
    private String offsetSwitch;


    @Override
    public ApiResult<CouponPlanVo> importCouponPlan(CouponPlanDto couponPlanDto) {
        //TODO 商品送错

        List<ProductMarket> productMarkets=productMarketService.findByTicketRule(Constants.ticketRule.VOUCHERS);
        List<MatchPlanDetailDto> matchPlanDetailDtos=couponPlanDto.getMatchPlanDetailDtos();
        BigDecimal totalPrice=BigDecimal.ZERO;
        //数据异常结果集
        List<String> planNos=new ArrayList<>();
        //商品编号集合
        List<String> productTradeNos=new ArrayList<>();
        //数据异常数量
        Integer failAmount=0;
        //数据正常数量
        Integer successAmount=0;
        CouponPlanVo couponPlanVo=new CouponPlanVo();
        couponPlanVo.setTotalAmout(matchPlanDetailDtos.size());
        for (int i = 0; i <matchPlanDetailDtos.size(); i++) {
            if(!checkProductTradeNo(matchPlanDetailDtos.get(i).getProductTradeNo(),productMarkets)){
                productTradeNos.add(matchPlanDetailDtos.get(i).getProductTradeNo());
            }
            Customer customer=customerService.findByCustomerNo(matchPlanDetailDtos.get(i).getCustomerNo());
            if(customer==null){
                planNos.add(matchPlanDetailDtos.get(i).getCustomerNo());
            }else {
                successAmount++;
                totalPrice=totalPrice.add(matchPlanDetailDtos.get(i).getPrice());
            }
        }
        if(planNos.size()>0||productTradeNos.size()>0){
            return ApiResult.badParam("出错的用户编号为"+JSONObject.toJSONString(planNos)+",出错的商品编号为"+JSONObject.toJSONString(productTradeNos));
        }
        CouponPlan couponPlan=new CouponPlan();
        couponPlan.setPlanTitle(couponPlanDto.getTitle());
        couponPlan.setTotalPrice(totalPrice);
        couponPlan.setStatus(Constants.MatchFailLogStatus.WAIT);
        couponPlan.setCreateTime(new Date());
        couponPlan.setModifyTime(new Date());
        couponPlan.setFlag(new Byte("1"));
        Integer line;
        try {
            line=tblCpCouponPlanMapper.insertSelective(couponPlan);
        }catch (DuplicateKeyException e){
            return ApiResult.error("计划名已存在");
        }
        if(line==1){
            for (int i = 0; i <matchPlanDetailDtos.size() ; i++) {
                ApiResult apiResult=matchPlanDetailService.insertList(matchPlanDetailDtos.get(i),couponPlan.getId());
                if(!apiResult.hasSuccess()){
                    //不成功
                    failAmount++;
                    planNos.add(matchPlanDetailDtos.get(i).getPlanNo());
                    successAmount--;
                    totalPrice=totalPrice.subtract(matchPlanDetailDtos.get(i).getPrice());
                }
            }
            couponPlanVo.setPlanNos(planNos);
            couponPlanVo.setFailAmount(failAmount);
            couponPlanVo.setSuccessAmount(successAmount);
            couponPlan.setTotalPrice(totalPrice);
            tblCpCouponPlanMapper.updateByPrimaryKeySelective(couponPlan);
            return ApiResult.success(couponPlanVo);
        }else {
            return ApiResult.error();
        }

    }

    @Override
    public ApiResult execute(CouponPlanExecuteDto couponPlanExecuteDto) {
        CouponPlan couponPlan=tblCpCouponPlanMapper.selectByPrimaryKey(couponPlanExecuteDto.getId());
        if(couponPlan==null){
            return ApiResult.badParam("计划不存在");
        }
        if(couponPlan.getStatus().equals(Constants.MatchFailLogStatus.DEAL)){
            return ApiResult.error("计划已执行");
        }
        log.info("开始执行配券计划，计划ID为"+couponPlanExecuteDto.getId());
        Condition condition=new Condition(MatchPlanDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("planId",couponPlanExecuteDto.getId());
        criteria.andEqualTo("status",Constants.CouponPlanDetailStatus.WAIT);
        List<MatchPlanDetail> matchPlanDetails=matchPlanDetailService.findByCondition(condition);
        for (int i = 0; i <matchPlanDetails.size(); i++) {
            MatchPlanDetail matchPlanDetail=matchPlanDetails.get(i);
            ChangeCouponAccountDto accountDto=new ChangeCouponAccountDto();
            accountDto.setCouponType(matchPlanDetail.getType());
            accountDto.setProductTradeNo(matchPlanDetail.getProductTradeNo());
            accountDto.setCustomerNo(matchPlanDetail.getCustomerNo());
            accountDto.setOrderNo(matchPlanDetail.getPlanNo());
            accountDto.setTradeType(CouponAccountTradeTypeEnum.ACTIVITY_GIVING.getCode());
            ApiResult usableApiResult=new ApiResult();
            ApiResult frozenApiResult=new ApiResult();
            ApiResult thawApiResult=new ApiResult();
            if(matchPlanDetail.getPrice().compareTo(BigDecimal.ZERO)!=0){
                if(matchPlanDetail.getPrice().compareTo(BigDecimal.ZERO)>0){
                    accountDto.setReType(new Byte("2"));
                    accountDto.setChangeAmount(matchPlanDetail.getPrice());
                }else if(matchPlanDetail.getPrice().compareTo(BigDecimal.ZERO)<0){
                    accountDto.setReType(new Byte("1"));
                    accountDto.setChangeAmount(BigDecimal.ZERO.subtract(matchPlanDetail.getPrice()));
                }

                //由于要插入不同的流水，则通过三次方法操作不同的余额（可用余额，冻结余额，解冻余额），先操作可用余额
                usableApiResult=couponAccountService.changeAmount(accountDto);
            }
            if(matchPlanDetail.getFrozenAmount().compareTo(BigDecimal.ZERO)>0){
                accountDto.setReType(new Byte("2"));
                accountDto.setChangeAmount(matchPlanDetail.getFrozenAmount());
                accountDto.setTradeType(CouponAccountTradeTypeEnum.FROZENAMOUT_IN.getCode());
                //再操作冻结金额
                frozenApiResult = couponAccountService.changeFrozenAmount(accountDto);
            }
            if(matchPlanDetail.getThawAmount().compareTo(BigDecimal.ZERO)>0){
                accountDto.setReType(new Byte("2"));
                accountDto.setChangeAmount(matchPlanDetail.getThawAmount());
                accountDto.setTradeType(CouponAccountTradeTypeEnum.THAWAMOUT_IN.getCode());
                //最后再操作解冻金额
                thawApiResult=couponAccountService.changeThawAmount(accountDto);
            }
            List<MatchPlanDetail> matchPlanDetailsUpdate=matchPlanDetailService.findByCustomerNoAndPlanId(matchPlanDetail.getCustomerNo(),couponPlanExecuteDto.getId());
            if (usableApiResult.hasSuccess()&&frozenApiResult.hasSuccess()&&thawApiResult.hasSuccess()){
                //成功之后将该计划ID之下的该用户的明细状态改为已执行
                for (int j = 0; j <matchPlanDetailsUpdate.size() ; j++) {
                    MatchPlanDetail planDetail=matchPlanDetailsUpdate.get(j);
                    planDetail.setModifyTime(new Date());
                    planDetail.setStatus(Constants.CouponPlanDetailStatus.SUCCESS);
                    matchPlanDetailService.update(planDetail);
                }
            }else {
                //修改失败则把状态改为失败
                for (int j = 0; j <matchPlanDetailsUpdate.size() ; j++) {
                    MatchPlanDetail planDetail=matchPlanDetailsUpdate.get(j);
                    planDetail.setModifyTime(new Date());
                    planDetail.setStatus(Constants.CouponPlanDetailStatus.FAIL);
                    matchPlanDetailService.update(planDetail);
                }
            }
        }
        couponPlan.setModifyTime(new Date());
        couponPlan.setDealTime(new Date());
        couponPlan.setStatus(Constants.MatchFailLogStatus.DEAL);
        tblCpCouponPlanMapper.updateByPrimaryKeySelective(couponPlan);
        return ApiResult.success();
    }

    @Override
    public void offsetCoupon(List<OffsetCouponDto> offsetCouponDtoList) {
        if (!Constants.MatchSwitch.ON.equals(offsetSwitch)) {
            throw new ServiceException("未打开开关");
        }
        /**
         * 基本流程：扣减用户券余额-->扣减用户账户余额（钱）-->新增用户持仓-->增加经销商账户余额-->扣减经销商持仓-->生成成交单-->插入流水
         */
        //查询经销商编码
        String memberCustomerNo = customerService.getDistributorNo();
        if(StringUtils.isEmpty(memberCustomerNo)){
            throw new ServiceException("经销商不存在");
        }
        //参数空格校验
        checkBlank(offsetCouponDtoList);
        List<CouponOffset> couponOffsetList = new ArrayList<>();
        for (OffsetCouponDto offsetCouponDto : offsetCouponDtoList) {
            ProductMarket productMarket = productMarketService.findByProductTradeNo(offsetCouponDto.getProductTradeNo());
            if (StringUtils.isEmpty(productMarket)) {
                throw new ServiceException("找不到指定交易商品，编码为："+offsetCouponDto.getProductTradeNo());
            }
            if((offsetCouponDto.getDetuchCouponAcct().add(offsetCouponDto.getOffsetAmount())).
                    divide(new BigDecimal(offsetCouponDto.getOffsetCount())).compareTo(productMarket.getIssuePrice())!=0){
                throw new ServiceException("资金异常，流水号为："+offsetCouponDto.getOffsetNo());
            }
            Date tradeDay;
            try {
                tradeDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(offsetCouponDto.getTradeDay());
            } catch (ParseException e) {
                e.printStackTrace();
                throw new ServiceException("交易日期转换错误");
            }
            //校验用户是否存在，不存在则直接报错，终止执行一切逻辑
            CustomerVo userByCustomer = customerService.findUserByCustomerNo(offsetCouponDto.getCustomerNo());
            if(StringUtils.isEmpty(userByCustomer)){
                throw  new ServiceException("查不到指定用户，用户编号为："+offsetCouponDto.getCustomerNo());
            }

            //查询用户账户信息
            Account account = accountService.checkAccount(offsetCouponDto.getCustomerNo());
            //查询用户券余额信息
            CouponAccount couponAccount = couponAccountService.getCouponAccount(offsetCouponDto.getCustomerNo(), offsetCouponDto.getProductTradeNo(), Constants.CouponType.VOUCHERS);
            //用户持仓信息
            HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(offsetCouponDto.getCustomerNo(), offsetCouponDto.getProductTradeNo(), Constants.HoldType.MAIN);
            if (!StringUtils.isEmpty(account)&&!StringUtils.isEmpty(couponAccount)) {
                //判断券余额和账户余额是否充足
                BigDecimal balance = account.getBalance();
                BigDecimal couponBalance = couponAccount.getBalance();
                BigDecimal shouldPay = offsetCouponDto.getOffsetAmount().add(offsetCouponDto.getBuyFee());
                if(couponBalance.compareTo(offsetCouponDto.getDetuchCouponAcct())>=0&&balance.compareTo(shouldPay)>=0){//券余额和账户余额充足的情况
                    //扣减用户券余额
                    changeCouponAmount(offsetCouponDto);
                    //扣减用户余额
                    changeAccountAmount(offsetCouponDto,shouldPay);
                    //新增或更新用户持仓
                    if(holdTotal==null){//新增用户持仓
                        addHoldTotal(offsetCouponDto);
                    }else{//更新用户持仓
                        boolean b = holdTotalService.addCustomerProductHoldForMatch(offsetCouponDto.getCustomerNo(), offsetCouponDto.getProductTradeNo(),
                                new BigDecimal(offsetCouponDto.getOffsetCount()), Constants.HoldType.MAIN);
                        if(!b)
                            throw new ServiceException("更新用户持仓失败，乐观锁问题，请联系管理员");
                    }
                    //新增用户持仓明细
                    addHoldDetails(offsetCouponDto,tradeDay);
                    //增加挂牌商余额
                    changeMemberAmount(offsetCouponDto.getOffsetNo(),memberCustomerNo,offsetCouponDto.getOffsetAmount(),FundTradeTypeEnum.MAIN_SELL.getCode()+"","2");
                    //扣减挂牌商余额（手续费）
                    if(offsetCouponDto.getSellFee().compareTo(new BigDecimal(0))>0){
                        changeMemberAmount(offsetCouponDto.getOffsetNo(),memberCustomerNo,offsetCouponDto.getSellFee(),FundTradeTypeEnum.OFFSET_SELL_FEE.getCode()+"","1");
                    }
                    //扣减经销商持仓明细
                    boolean b1 = holdDetailsService.deductProductHoldByTradeTime(memberCustomerNo, offsetCouponDto.getProductTradeNo(), new BigDecimal(offsetCouponDto.getOffsetCount()), Constants.HoldType.MAIN);
                    if(!b1)
                        throw new ServiceException("扣减挂牌商商品持仓明细乐观锁问题");

                  /*  //扣减经销商持仓
                    boolean b = holdTotalService.deductMemberProductHold(memberCustomerNo, offsetCouponDto.getProductTradeNo(), new BigDecimal(offsetCouponDto.getOffsetCount()), Constants.HoldType.MAIN);
                    if(!b)
                        throw new ServiceException("扣减挂牌商商品总持仓乐观锁问题");*/
                    //生成成交单
                    createDealOrder(offsetCouponDto,memberCustomerNo);
                    //插入流水
                    CouponOffset couponOffset = getCouponOffset(offsetCouponDto, tradeDay,Constants.Status.SUCCESS,null);
                    couponOffsetList.add(couponOffset);
                }else{
                    String failType="";
                    if(couponBalance.compareTo(offsetCouponDto.getDetuchCouponAcct())<0){
                        failType=Constants.OffsetFailType.COUPONBALANCE_LIMIT;
                    }
                    if(balance.compareTo(shouldPay)<0){
                        failType=Constants.OffsetFailType.BALANCE_LIMIT;
                    }
                    CouponOffset couponOffset = getCouponOffset(offsetCouponDto, tradeDay,Constants.Status.FAIL,failType);
                    couponOffsetList.add(couponOffset);
                }
            }else{
                CouponOffset couponOffset = getCouponOffset(offsetCouponDto, tradeDay,Constants.Status.FAIL,Constants.OffsetFailType.DATA_ERROR);
                couponOffsetList.add(couponOffset);
            }
        }
        //插入流水
        if(couponOffsetList.size()>0){
            couponOffsetService.save(couponOffsetList);
        }
    }

    public CouponOffset getCouponOffset(OffsetCouponDto offsetCouponDto,Date tradeDay,String status,String failType){
        CouponOffset couponOffset = new CouponOffset();
        couponOffset.setOffsetNo(offsetCouponDto.getOffsetNo());
        couponOffset.setCustomerNo(offsetCouponDto.getCustomerNo());
        couponOffset.setProductTradeNo(offsetCouponDto.getProductTradeNo());
        couponOffset.setDetuchCouponAcct(offsetCouponDto.getDetuchCouponAcct());
        couponOffset.setOffsetAmount(offsetCouponDto.getOffsetAmount());
        couponOffset.setOffsetCount(offsetCouponDto.getOffsetCount());
        couponOffset.setCost(offsetCouponDto.getCost());
        couponOffset.setBuyFee(offsetCouponDto.getBuyFee());
        couponOffset.setSellFee(offsetCouponDto.getSellFee());
        couponOffset.setTradeDay(tradeDay);
        couponOffset.setStatus(status);
        if (!StringUtils.isEmpty(failType)) {
            couponOffset.setFailType(failType);
        }
        couponOffset.setCreateTime(new Date());
        couponOffset.setModifyTime(new Date());
        couponOffset.setFlag(new Byte(Constants.Flag.VALID));
        return couponOffset;
    }

    private void createDealOrder(OffsetCouponDto offsetCouponDto,String memberCustomerNo) {
        DealOrder dealOrder = new DealOrder();
        dealOrder.setDealNo(offsetCouponDto.getOffsetNo());
        dealOrder.setEntrustId(0L);
        dealOrder.setProductTradeNo(offsetCouponDto.getProductTradeNo());
        dealOrder.setBuyCustomerNo(offsetCouponDto.getCustomerNo());
        dealOrder.setSellCustomerNo(memberCustomerNo);//挂牌商
        dealOrder.setBuyFee(offsetCouponDto.getBuyFee());
        dealOrder.setType(Constants.TradeDirection.OFFSET);
        dealOrder.setPrice(offsetCouponDto.getOffsetAmount().divide(new BigDecimal(offsetCouponDto.getOffsetCount())));
        dealOrder.setCount(offsetCouponDto.getOffsetCount());
        dealOrder.setHoldType(Constants.HoldType.MAIN);
        dealOrderService.save(dealOrder);
    }

    /**
     * 更新挂牌商余额
     */
    public void changeMemberAmount(String offsetNo,String memberCustomerNo,BigDecimal changeAmount,String tradeType,String reType){
        //查询挂牌商编码
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(memberCustomerNo);
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setOrderNo(offsetNo);
        changeAmountDto.setTradeType(Byte.valueOf(tradeType));
        changeAmountDto.setReType(Byte.valueOf(reType));
        accountService.changeAccount(changeAmountDto);
    }


    /**
     * 新增持仓明细
     * @param matchDto
     * @param cost
     * @param theFifthTradeDay
     */
    public void addHoldDetails(OffsetCouponDto offsetCouponDto,Date tradeDay){
        HoldDetails holdDetails = new HoldDetails();
        holdDetails.setCustomerNo(offsetCouponDto.getCustomerNo());
        holdDetails.setProductTradeNo(offsetCouponDto.getProductTradeNo());
        holdDetails.setOriginalCount(new BigDecimal(offsetCouponDto.getOffsetCount()));
        holdDetails.setFrozenCount(new BigDecimal(0));
        holdDetails.setRemaindCount(new BigDecimal(offsetCouponDto.getOffsetCount()));
        holdDetails.setCost(offsetCouponDto.getCost());
        holdDetails.setHoldTime(new Date());
        holdDetails.setHoldNo(offsetCouponDto.getOffsetNo());
        holdDetails.setResource(Constants.HoldResource.OFFSET);
        holdDetails.setScanner(Byte.valueOf("0"));
        holdDetails.setHoldType(Constants.HoldType.MAIN);
        holdDetails.setCreateTime(new Date());
        holdDetails.setModifyTime(new Date());
        holdDetails.setTradeTime(tradeDay);//交易日期
        holdDetails.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdDetailsMapper.insert(holdDetails);
    }

    /**
     * 新增用户持仓
     * @param matchDto
     * @param cost
     */
    public void addHoldTotal(OffsetCouponDto offsetCouponDto){
        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setCustomerNo(offsetCouponDto.getCustomerNo());
        newHoldTotal.setProductTradeNo(offsetCouponDto.getProductTradeNo());
        newHoldTotal.setTotalCount(new BigDecimal(offsetCouponDto.getOffsetCount()));
        newHoldTotal.setFrozenCount(new BigDecimal(0));
        newHoldTotal.setCanSellCount(new BigDecimal(0));
        newHoldTotal.setCost(new BigDecimal(0));
        newHoldTotal.setHoldType(Constants.HoldType.MAIN);
        newHoldTotal.setCreateTime(new Date());
        newHoldTotal.setModifyTime(new Date());
        newHoldTotal.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdTotalMapper.insert(newHoldTotal);
    }

    /**
     * 扣减账户余额
     * @param offsetCouponDto
     * @param changeAmount
     */
    public void changeAccountAmount(OffsetCouponDto offsetCouponDto,BigDecimal changeAmount){
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(offsetCouponDto.getCustomerNo());
        changeAmountDto.setOrderNo(offsetCouponDto.getOffsetNo());
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setTradeType(FundTradeTypeEnum.MAIN_BUY.getCode());
        changeAmountDto.setReType(new Byte("1"));
        accountService.changeAccount(changeAmountDto);
    }

    /**
     * 扣减券余额
     * @param offsetCouponDto
     */
    public void changeCouponAmount(OffsetCouponDto offsetCouponDto){
        ChangeCouponAccountDto changeCouponAccountDto = new ChangeCouponAccountDto();
        changeCouponAccountDto.setCustomerNo(offsetCouponDto.getCustomerNo());
        changeCouponAccountDto.setProductTradeNo(offsetCouponDto.getProductTradeNo());
        changeCouponAccountDto.setOrderNo(offsetCouponDto.getOffsetNo());
        changeCouponAccountDto.setChangeAmount(offsetCouponDto.getDetuchCouponAcct());
        changeCouponAccountDto.setCouponType(Constants.CouponType.VOUCHERS);
        changeCouponAccountDto.setTradeType(CouponAccountTradeTypeEnum.OFFSET_CONSUME.getCode());
        changeCouponAccountDto.setReType(new Byte("1"));
        couponAccountService.changeAmount(changeCouponAccountDto);
    }

    public void checkBlank(List<OffsetCouponDto> offsetCouponDtoList){
        List<OffsetCouponDto> errorInfoList = new ArrayList<>();
        for (OffsetCouponDto offsetCouponDto : offsetCouponDtoList) {
            if(offsetCouponDto.getCustomerNo().indexOf(" ")!=-1 ||offsetCouponDto.getOffsetNo().indexOf(" ")!=-1
                    ||offsetCouponDto.getProductTradeNo().indexOf(" ")!=-1){//字符串包含空格
                errorInfoList.add(offsetCouponDto);
            }
            if(errorInfoList.size()>0){
                throw new ServiceException("配票失败，字符串参数存在空格，错误信息有："+JSON.toJSONString(errorInfoList));
            }
        }
    }

    private Boolean checkProductTradeNo(String productTradeNo,List<ProductMarket> productMarkets){
        Boolean flag=false;
        for (int i = 0; i <productMarkets.size() ; i++) {
            if (productTradeNo.equals(productMarkets.get(i).getProductTradeNo())){
                flag=true;
                break;
            }
        }
        return flag;
    }
}
