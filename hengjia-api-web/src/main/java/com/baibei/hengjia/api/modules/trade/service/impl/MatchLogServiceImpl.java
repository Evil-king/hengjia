package com.baibei.hengjia.api.modules.trade.service.impl;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.model.Account;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.product.model.ProductMarket;
import com.baibei.hengjia.api.modules.product.model.ProductStock;
import com.baibei.hengjia.api.modules.product.service.IProductMarketService;
import com.baibei.hengjia.api.modules.product.service.IProductStockService;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryApplyDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryQueryDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchDto;
import com.baibei.hengjia.api.modules.trade.bean.dto.MatchListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MatchVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryVo;
import com.baibei.hengjia.api.modules.trade.dao.*;
import com.baibei.hengjia.api.modules.trade.model.*;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.api.modules.user.bean.vo.CustomerVo;
import com.baibei.hengjia.api.modules.user.model.CustomerAddress;
import com.baibei.hengjia.api.modules.user.service.ICustomerAddressService;
import com.baibei.hengjia.api.modules.user.service.ICustomerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FreezingAmountTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.page.PageParam;
import com.baibei.hengjia.common.tool.page.PageUtil;
import com.baibei.hengjia.common.tool.utils.IdWorker;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.Bidi;
import java.util.*;


/**
* @author: Longer
* @date: 2019/06/10 14:09:05
* @description: 配票相关
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class MatchLogServiceImpl extends AbstractService<MatchLog> implements IMatchLogService {

    @Autowired
    private IProductStockService productStockService;
    @Autowired
    private IHoldTotalService holdTotalService;
    @Autowired
    private IHoldDetailsService holdDetailsService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private HoldTotalMapper holdTotalMapper;
    @Autowired
    private HoldDetailsMapper holdDetailsMapper;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private MatchLogMapper matchLogMapper;
    @Autowired
    private IMatchConfigService matchConfigService;
    @Autowired
    private IDeliveryService deliveryService;
    @Autowired
    private ICustomerAddressService customerAddressService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private ISigningRecordService signingRecordService;

    @Value("${delivery.match.count}")
    private String deliveryMatchCount;//提货配票赠送数量

    @Value("${match.fee.rate}")
    private String matchFeeRate;//配票手续费率

    /**
     * 交易配票
     * @param matchListDto
     * @return
     */
    @Override
    public MatchVo match(MatchListDto matchListDto) {
        log.info("=====开始执行交易配票业务=====");
        //判断当前时间是否休市状态
        boolean tradeDay = tradeDayService.isTradeTime();
        if(tradeDay){
            throw new ServiceException("未到休市时间不能够进行交易配票");
        }
        /**
         * 配票大致流程：更新用户持仓信息-》新增持仓明细-》扣减账户余额-》新增配票日志-》返回账户余额不足的配票信息
         */
        List<MatchDto> matchDtoList = matchListDto.getMatchDtoList();
        MatchVo matchVo = new MatchVo();
        List<MatchDto> disMatchList = matchVo.getDisMatchList();
        //校验参数是否合法
        checkBlank(matchDtoList);
        //获取第六个交易日信息
        TradeDay theFifthTradeDay = tradeDayService.getTheFifthTradeDay();
        for (MatchDto matchDto : matchDtoList) {
            //校验用户是否存在，不存在则直接报错，终止执行一切逻辑
            CustomerVo userByCustomer = customerService.findUserByCustomerNo(matchDto.getCustomerNo().trim());
            if(StringUtils.isEmpty(userByCustomer)){
                throw  new ServiceException("查不到指定用户，用户编号为："+matchDto.getCustomerNo());
            }
            //用户签约信息
            SigningRecord signingRecord = signingRecordService.findByCustAcctId(matchDto.getCustomerNo());
            if(signingRecord!=null){//只有已签约的用户才能进行配票
                //查询挂牌商编码
                String memberCustomerNo = customerService.getMemberCustomerNoByProductNo(matchDto.getProductTradeNo());
                if(StringUtils.isEmpty(memberCustomerNo)){
                    throw new ServiceException("挂牌商不存在，商品交易编码为："+matchDto.getProductTradeNo());
                }
                String matchStatus="";
                //查询商品的成本价
                ProductStock productStock = productStockService.findByProductTradeNo(matchDto.getProductTradeNo());
                if(StringUtils.isEmpty(productStock)){
                    throw new ServiceException("商品不存在，商品交易编码为："+matchDto.getProductTradeNo());
                }
                //成本价
                BigDecimal cost = productStock.getCost();
                //用户持仓信息
                HoldTotal holdTotal = holdTotalService.findByCustomerAndProductNo(matchDto.getCustomerNo(), matchDto.getProductTradeNo(), Constants.HoldType.MATCH);
                //应扣减钱数=配票数量*商品成本价+手续费{（配票数量*商品成本价）*0.003}
                BigDecimal productPay = matchDto.getMatchNum().divide(new BigDecimal(1),1,BigDecimal.ROUND_DOWN).multiply(cost)
                        .divide(new BigDecimal("1"),2,BigDecimal.ROUND_UP);//配票数量*商品成本价
                //手续费
                BigDecimal fee = matchDto.getMatchNum().divide(new BigDecimal(1),1,BigDecimal.ROUND_DOWN).multiply(cost).multiply(new BigDecimal(matchFeeRate))
                        .divide(new BigDecimal("1"),2,BigDecimal.ROUND_UP);
                BigDecimal shouldPay = productPay.add(fee).divide(new BigDecimal("1"),2,BigDecimal.ROUND_UP);//应扣减钱数
                //查询用户账户信息
                Account account = accountService.checkAccount(matchDto.getCustomerNo());
                BigDecimal balance = account.getBalance();//可用余额
                if(balance.compareTo(shouldPay)<0){//余额不足
                    //计算当前余额可以配送多少张票
                    BigDecimal canMatchNum = balance.divide((cost.add(cost.multiply(new BigDecimal(matchFeeRate)))), 1, BigDecimal.ROUND_DOWN);
                    //成本支出
                    BigDecimal costPay = canMatchNum.multiply(cost).divide(new BigDecimal("1"),2,BigDecimal.ROUND_UP);
                    //手续费支出
                    BigDecimal feePay = canMatchNum.multiply(cost).multiply(new BigDecimal(matchFeeRate))
                            .divide(new BigDecimal("1"),2,BigDecimal.ROUND_UP);
                    matchDto.setMatchMoney(costPay.add(feePay));
                    matchDto.setCost(costPay);
                    matchDto.setFee(feePay);
                    matchDto.setMatchSuccessNum(canMatchNum);
                    matchStatus=canMatchNum.compareTo(new BigDecimal(0))==0?Constants.MatchStatus.FAIL:Constants.MatchStatus.HALF_SUCCESS;
                    matchDto.setMatchStatus(matchStatus);
                    disMatchList.add(matchDto);
                    //新增挂牌商余额
                    if(costPay.compareTo(new BigDecimal("0"))>0)
                        changeMemberAmount(matchDto.getMatchNo(),memberCustomerNo,costPay,FundTradeTypeEnum.MATCH_SELL.getCode()+"","2");
                }else{
                    matchDto.setMatchMoney(shouldPay);
                    matchDto.setCost(productPay);
                    matchDto.setFee(fee);
                    matchDto.setMatchSuccessNum(matchDto.getMatchNum());
                    matchStatus=Constants.MatchStatus.SUCCESS;
                    matchDto.setMatchStatus(matchStatus);
                    disMatchList.add(matchDto);
                    //新增挂牌商余额
                    if(productPay.compareTo(new BigDecimal("0"))>0)
                        changeMemberAmount(matchDto.getMatchNo(),memberCustomerNo,productPay,FundTradeTypeEnum.MATCH_SELL.getCode()+"","2");
                }
                log.info("=====交易配票，操作持仓信息=====");
                if(matchDto.getMatchSuccessNum().compareTo(new BigDecimal(0))>0){
                    //新增或更新用户持仓
                    if(holdTotal==null){//新增用户持仓
                        addHoldTotal(matchDto,cost);
                    }else{//更新用户持仓
                        boolean b = holdTotalService.addCustomerProductHoldForMatch(matchDto.getCustomerNo(), matchDto.getProductTradeNo(),
                                matchDto.getMatchSuccessNum(), Constants.HoldType.MATCH);
                        if(!b)
                            throw new ServiceException("配票失败，乐观锁问题，请联系管理员");
                    }
                    log.info("=====交易配票，操作持仓明细信息=====");
                    //新增持仓明细
                    addHoldDetails(matchDto,cost,theFifthTradeDay);

                    //扣减挂牌商持仓
                    boolean b = holdTotalService.deductMemberProductHold(memberCustomerNo, matchDto.getProductTradeNo(), matchDto.getMatchSuccessNum(), Constants.HoldType.MAIN);
                    if(!b)
                        throw new ServiceException("配票失败，扣减挂牌商商品总持仓乐观锁问题");
                    //扣减挂牌商持仓明细
                    boolean b1 = holdDetailsService.deductMemberProductHold(memberCustomerNo, matchDto.getProductTradeNo(), matchDto.getMatchSuccessNum(), Constants.HoldType.MAIN);
                    if(!b1)
                        throw new ServiceException("配票失败，扣减挂牌商商品持仓明细乐观锁问题");
                    log.info("=====交易配票，操作用户余额信息=====");
                    //更新账户余额
                    changeAmount(matchDto);
                }
                log.info("=====新增配票记录=====");
                //新增配票记录
                addMatchLog(matchDto,matchStatus);
            }
        }
        log.info("=====结束执行交易配票业务=====");
        return matchVo;
    }

    /**
     * 提货配票(未加乐观锁)
     */
    @Override
    public void deliveryMatch() {

       /* //判断当前时间是否休市状态
        boolean tradeDay = tradeDayService.isTradeTime();
        if(tradeDay){
            throw new ServiceException("未到休市时间不能够进行提货配票");
        }
        //判断开关是否打开
        MatchConfig aSwitch = matchConfigService.getSwitch(Constants.SwitchType.DELIVERYMATCH);
        if(aSwitch.getMatchSwitch().equals(Constants.MatchSwitch.OFF))
            throw new ServiceException("提货配票开关没打开");
        if(aSwitch.getMatchSwitch().equals(Constants.MatchSwitch.ON)){
            //获取符合规则的用户集合(还未为该用户提货配票过)
            List<String> customerNos = this.getCustomerNos();
            //获取第六个交易日信息
            System.out.println(tradeDayService == null);
            TradeDay theFifthTradeDay = tradeDayService.getTheFifthTradeDay();
            System.out.println(theFifthTradeDay == null);
            for (String customerNo : customerNos) {
                //用户签约信息
                SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerNo);
                if(signingRecord!=null){
                    *//**
                     * 提货配票流程：查询满足条件的商品--》更新用户持仓信息(扣除100张本票，新增105张本票（来源为：赠送）)-》新增持仓明细-》生成提货订单-》新增配票日志
                     *//*
                    //查询满足条件的商品
                    List<HoldTotal> holdTotalList = holdTotalService.matchHoldList(customerNo);
                    //查询用户的收货地址
                    CustomerAddress customerAddress = customerAddressService.findDefaultByNo(customerNo);
                    if(customerAddress!=null){//有收货地址才配
                        for (HoldTotal holdTotal : holdTotalList) {
                            String deliveryFlag="success";//提货是否成功标识
                            //查看用户是否已经提过货
                            Delivery deliveryByProAndCust = deliveryService.getDeliveryByProAndCust(customerNo, holdTotal.getProductTradeNo());
                            if(deliveryByProAndCust==null){
                                //生成提货订单
                                DeliveryApplyDto deliveryApplyDto = new DeliveryApplyDto();
                                deliveryApplyDto.setProductTradeNo(holdTotal.getProductTradeNo());
                                deliveryApplyDto.setHoldType(Constants.HoldType.MAIN);
                                deliveryApplyDto.setDeliveryCount(100);//提货100手
                                deliveryApplyDto.setAddressId(customerAddress.getId());
                                deliveryApplyDto.setCustomerNo(customerNo);
                                deliveryApplyDto.setRemark("提货配票");
                                deliveryApplyDto.setValidateTradeDayFlag("1");
                                ApiResult apiResult = deliveryService.deliveryApply(deliveryApplyDto);
                                if(ResultEnum.ERROR.getCode()==apiResult.getCode()){
                                    deliveryFlag="fail";
                                    log.info("用户提货配票失败，该用户编号为：{}，提货商品为：{}，失败原因为：{}",customerNo,holdTotal.getProductTradeNo(),apiResult.getMsg());
                                }
                            }
                            if(deliveryFlag.equals("success")){
                                //更新持仓单持仓单
                                holdTotal.setTotalCount(holdTotal.getTotalCount().add(new BigDecimal(deliveryMatchCount)));
                                holdTotalService.updateTotalCountById(holdTotal.getId(),deliveryMatchCount);

                                MatchDto matchDto = new MatchDto();
                                matchDto.setCustomerNo(customerNo);
                                matchDto.setMatchNo(IdWorker.get32UUID());
                                matchDto.setProductTradeNo(holdTotal.getProductTradeNo());
                                matchDto.setMatchType(Constants.MatchType.SEND);
                                matchDto.setMatchNum(new BigDecimal(deliveryMatchCount));
                                matchDto.setMatchStatus(Constants.MatchStatus.SUCCESS);
                                matchDto.setMatchSuccessNum(new BigDecimal(deliveryMatchCount));
                                *//*addHoldTotal(matchDto,holdTotal.getCost());*//*
                                //插入持仓明细
                                addHoldDetails(matchDto,holdTotal.getCost(),theFifthTradeDay);
                                //新增配票日志
                                addMatchLog(matchDto,Constants.MatchStatus.SUCCESS);
                                //查询挂牌商编码
                                String memberCustomerNo = customerService.getMemberCustomerNoByProductNo(holdTotal.getProductTradeNo());
                                //扣减挂牌商持仓
                                boolean b = holdTotalService.deductMemberProductHold(memberCustomerNo, matchDto.getProductTradeNo(), matchDto.getMatchSuccessNum(), Constants.HoldType.MAIN);
                                if(!b)
                                    throw new ServiceException("提货配票失败，扣减挂牌商商品总持仓乐观锁问题");
                                //扣减挂牌商持仓明细
                                boolean b1 = holdDetailsService.deductMemberProductHold(memberCustomerNo, matchDto.getProductTradeNo(), matchDto.getMatchSuccessNum(), Constants.HoldType.MAIN);
                                if(!b1)
                                    throw new ServiceException("配票失败，扣减挂牌商商品持仓明细乐观锁问题");
                            }

                        }
                    }
                }
            }
        }
        log.info("提货配票逻辑执行完毕，关闭提货配票开关");
        //关闭提货配票开关
        int i = matchConfigService.matchSwitch(Constants.MatchSwitch.OFF, Constants.SwitchType.DELIVERYMATCH);
        if(i==0)
            throw new ServiceException("提货配票失败，开关乐观锁问题");*/
    }

    @Override
    public List<String> getCustomerNos() {
        List<String> customerNos=matchLogMapper.selectCustomerNos();
        return customerNos;
    }

    @Override
    public List<CustomerCountVo> sumFee(Map<String, Object> param) {
        return matchLogMapper.sumFee(param);
    }

    @Override
    public List<CustomerCountVo> sumLoss(Map<String, Object> param) {
        return matchLogMapper.sumLoss(param);
    }

    @Override
    public BigDecimal sumCost(Map<String, Object> param) {
        return matchLogMapper.sumCost(param);
    }

    /**
     * 新增用户持仓
     * @param matchDto
     * @param cost
     */
    public void addHoldTotal(MatchDto matchDto,BigDecimal cost){
        HoldTotal newHoldTotal = new HoldTotal();
        newHoldTotal.setCustomerNo(matchDto.getCustomerNo());
        newHoldTotal.setProductTradeNo(matchDto.getProductTradeNo());
        newHoldTotal.setTotalCount(matchDto.getMatchSuccessNum());
        newHoldTotal.setFrozenCount(new BigDecimal(0));
        newHoldTotal.setCanSellCount(new BigDecimal(0));
        newHoldTotal.setCost(cost);
        newHoldTotal.setHoldType(!Constants.MatchType.SEND.equals(matchDto.getMatchType())?Constants.HoldType.MATCH:Constants.HoldType.MAIN);
        newHoldTotal.setCreateTime(new Date());
        newHoldTotal.setModifyTime(new Date());
        newHoldTotal.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdTotalMapper.insert(newHoldTotal);
    }

    /**
     * 新增持仓明细
     * @param matchDto
     * @param cost
     * @param theFifthTradeDay
     */
    public void addHoldDetails(MatchDto matchDto,BigDecimal cost,TradeDay theFifthTradeDay){
        HoldDetails holdDetails = new HoldDetails();
        holdDetails.setCustomerNo(matchDto.getCustomerNo());
        holdDetails.setProductTradeNo(matchDto.getProductTradeNo());
        holdDetails.setOriginalCount(matchDto.getMatchSuccessNum());
        holdDetails.setFrozenCount(new BigDecimal(0));
        holdDetails.setRemaindCount(matchDto.getMatchSuccessNum());
        holdDetails.setCost(cost);
        holdDetails.setHoldTime(new Date());
        holdDetails.setHoldNo(matchDto.getMatchNo());
        holdDetails.setResource(Constants.HoldResource.PLAN);
        holdDetails.setScanner(Byte.valueOf("0"));
        holdDetails.setHoldType(!Constants.MatchType.SEND.equals(matchDto.getMatchType())?Constants.HoldType.MATCH:Constants.HoldType.MAIN);
        holdDetails.setCreateTime(new Date());
        holdDetails.setModifyTime(new Date());
        holdDetails.setTradeTime(theFifthTradeDay.getTradeDay());//交易日期
        holdDetails.setFlag(Byte.valueOf(Constants.Flag.VALID));
        holdDetailsMapper.insert(holdDetails);
    }

    /**
     * 更新账户余额
     * @param matchDto
     */
    public void changeAmount(MatchDto matchDto){
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(matchDto.getCustomerNo());
        changeAmountDto.setChangeAmount(matchDto.getMatchMoney());
        changeAmountDto.setOrderNo(matchDto.getMatchNo());
        changeAmountDto.setTradeType(Byte.valueOf(FundTradeTypeEnum.MATCH_BUY.getCode()+""));
        changeAmountDto.setReType(Byte.valueOf("1"));
        accountService.changeAccount(changeAmountDto);
    }

    /**
     * 更新挂牌商余额
     */
    public void changeMemberAmount(String matchNo,String memberCustomerNo,BigDecimal changeAmount,String tradeType,String reType){
        //查询挂牌商编码
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(memberCustomerNo);
        changeAmountDto.setChangeAmount(changeAmount);
        changeAmountDto.setOrderNo(matchNo);
        changeAmountDto.setTradeType(Byte.valueOf(tradeType));
        changeAmountDto.setReType(Byte.valueOf(reType));
        accountService.changeAccount(changeAmountDto);
    }

    public void addMatchLog(MatchDto matchDto,String matchStatus){
        MatchLog matchLog = new MatchLog();
        matchLog.setMatchNo(matchDto.getMatchNo());
        matchLog.setCustomerNo(matchDto.getCustomerNo());
        matchLog.setProductTradeNo(matchDto.getProductTradeNo());
        matchLog.setMatchType(matchDto.getMatchType());
        matchLog.setMatchNum(matchDto.getMatchNum());
        matchLog.setMatchSuccessNum(matchDto.getMatchSuccessNum());
        matchLog.setMatchMoney(matchDto.getMatchMoney()==null?new BigDecimal(0):matchDto.getMatchMoney());
        matchLog.setCost(matchDto.getCost()==null?new BigDecimal(0):matchDto.getCost());
        matchLog.setFee(matchDto.getFee()==null?new BigDecimal(0):matchDto.getFee());
        matchLog.setPeriod(new Date());
        matchLog.setRemark(matchDto.getRemark());
        matchLog.setCreateTime(new Date());
        matchLog.setModifyTime(new Date());
        matchLog.setMatchStatus(matchStatus);
        matchLog.setFlag(Integer.parseInt(Constants.Flag.VALID));
        matchLogMapper.insert(matchLog);
    }
    public void checkBlank(List<MatchDto> matchDtoList){
        List<MatchDto> errorInfoList = new ArrayList<>();
        for (MatchDto matchDto : matchDtoList) {
            int i = matchDto.getCustomerNo().indexOf(" ");
            if(matchDto.getCustomerNo().indexOf(" ")!=-1||matchDto.getMatchNo().indexOf(" ")!=-1
                    ||matchDto.getMatchType().indexOf(" ")!=-1){//字符串包含空格
                errorInfoList.add(matchDto);
            }
            if(errorInfoList.size()>0){
                throw new ServiceException("配票失败，字符串参数存在空格，错误信息有："+JSON.toJSONString(errorInfoList));
            }
        }
    }

   /* public static void main(String[] args) {
        String str=" abd dddc";  //待判断的字符串
        int i = str.indexOf(" ");
        System.out.println(i);
        if(str.indexOf(" ")!=-1){
            System.out.print("There are spaces! "+i);
        }
    }*/
}
