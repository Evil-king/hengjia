package com.baibei.hengjia.api.modules.settlement.biz;

import com.baibei.hengjia.api.modules.account.bean.dto.ChangeAmountDto;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CleanVo;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.settlement.model.AmountReturn;
import com.baibei.hengjia.api.modules.settlement.service.IAmountReturnService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.api.modules.shop.service.IShpOrderService;
import com.baibei.hengjia.api.modules.sms.util.RandomUtils;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.api.modules.trade.model.MatchConfig;
import com.baibei.hengjia.api.modules.trade.service.*;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/2 4:50 PM
 * @description:
 */
@Component
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AmountReturnBiz {
    // 手续费返还1比例
    @Value("${fee1.return.rate}")
    private String fee1ReturnRate;
    // 手续费返还1交易商账号
    @Value("${fee1.return.customerno}")
    private String fee1ReturnCustomerNo;
    // 盈利转积分结转账户
    @Value("${integral.return.customerno}")
    private String integralReturnCustomerNo;

    @Value("${integral.rate}")
    private String integralReturnRate;

    // 交易手续费计提待返比例
    @Value("${jt.rate}")
    private String jtRate;

    @Value("${settlement.credit.time}")
    private String creditTime;

    @Value("${settlement.credit.rate}")
    private String creditRate;

    @Value("${settlement.credit.customerNo}")
    private String creditCustomerNo;
    @Autowired
    private IMatchConfigService matchConfigService;

    @Autowired
    private IAmountReturnService amountReturnService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private IMatchLogService matchLogService;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private IDeliveryService deliveryService;
    @Autowired
    private IShpOrderService shpOrderService;

    private static final BigDecimal oneHundred = new BigDecimal("100");
    @Autowired
    private IAccountService accountService;


    /**
     * 数据计算处理
     */
    public void generate() {
        Date now = new Date();
        /*// step1.判断今日是否为交易日
        boolean isTradeDay = tradeDayService.isTradeDay(now);
        if (!isTradeDay) {
            log.info("非交易日,不进行业务办理");
            return;
        }*/
        String batchNo = DateUtil.yyyyMMddNoLine.get().format(now);
        // 返回手续费数据计算
        feeReturn(batchNo);
        // 返还积分数据计算
        //integralReturn(batchNo);
        // 提货款数据计算
        creditReturn(batchNo);
        // 更新清算进度状态
        cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.AMOUNT_RETURN, Constants.CleanFlowPathStatus.COMPLETED);
    }

    /**
     * 返回手续费数据计算
     *
     * @param batchNo
     */
    private void feeReturn(String batchNo) {
        Date tradeDay;
        try {
            tradeDay = DateUtil.yyyyMMddNoLine.get().parse(batchNo);
        } catch (ParseException e) {
            throw new ServiceException("batchNo格式错误");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("beginTime", DateUtil.getBeginDay(tradeDay));
        param.put("endTime", DateUtil.getEndDay(tradeDay));
        // step1.查询系统签约存管用户,清算要求全量对账
        List<SigningRecord> signingRecordList = signingRecordService.allList();
        if (CollectionUtils.isEmpty(signingRecordList)) {
            log.info("签约用户为空,不予清算");
            return;
        }
        List<CleanVo> cleanVoList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        // step2.构建清算bean
        cleanVoList.addAll(signingRecordList.stream().map(signingRecord -> buildCleanVoBaseInfo(atomicInteger.getAndIncrement(), signingRecord.getCustAcctId(),
                signingRecord.getCustName(), signingRecord.getThirdCustId(), batchNo, signingRecord.getFuncFlag())).collect(Collectors.toList()));
        // step5.统计客户所有手续费总和,总手续费=买入手续费+卖出手续费+提现手续费+配票手续费
        // 统计买入手续费+卖出手续费
        List<CustomerCountVo> tradeFeeList = dealOrderService.sumFee(param);
        for (CustomerCountVo customerCountVo : tradeFeeList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (customerCountVo.getCustomerNo().equals(cleanVo.getThirdCustId())) {
                    cleanVo.setTotalFee(cleanVo.getTotalFee().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }
        // 统计提现手续费
        List<CustomerCountVo> withdrawFeeList = orderWithdrawService.sumFee(param);
        for (CustomerCountVo customerCountVo : withdrawFeeList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (customerCountVo.getCustomerNo().equals(cleanVo.getThirdCustId())) {
                    cleanVo.setTotalFee(cleanVo.getTotalFee().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }
        // 统计配票手续费
        List<CustomerCountVo> matchFeeList = matchLogService.sumFee(param);
        for (CustomerCountVo customerCountVo : matchFeeList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (customerCountVo.getCustomerNo().trim().equals(cleanVo.getThirdCustId())) {
                    cleanVo.setTotalFee(cleanVo.getTotalFee().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }
        // step6.获取当天应支付给交易网的交易手续费总额,公式:当天的手续费合计*（1-计提待返比例）（保留两位小数后，截断）
        for (CleanVo cleanVo : cleanVoList) {
            if (cleanVo.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
                cleanVo.setTranHandFee(NumberUtil.roundFloor(cleanVo.getTotalFee().multiply((new BigDecimal("1").
                        subtract(new BigDecimal(jtRate).divide(oneHundred)))), 2));
            }
        }
        // 全盘总的手续费
        BigDecimal totalFee = BigDecimal.ZERO;
        BigDecimal totalTranHandFee = BigDecimal.ZERO;
        for (CleanVo item : cleanVoList) {
            totalFee = totalFee.add(item.getTotalFee());
            totalTranHandFee = totalTranHandFee.add(item.getTranHandFee());
        }
        // 计算手续费返还用户的盈利总金额,返还用户：∑（卖出数量*卖出价格）-采购专款（自己）+手续费返还金额。注：手续费返还金额=当天总手续费合计*返还比例
        for (CleanVo cleanVo : cleanVoList) {
            if (cleanVo.getThirdCustId().equals(fee1ReturnCustomerNo)) {
                log.info("手续费返还用户,fee1ReturnCustomerNo={},totalFee={},fee1ReturnRate={},totalTranHandFee={}",
                        fee1ReturnCustomerNo, totalFee, fee1ReturnRate, totalTranHandFee);
                BigDecimal returnAmount1 = totalFee.subtract(totalTranHandFee);
                // 入库,存在则先删除
                List<AmountReturn> list = amountReturnService.findByBatchNo(batchNo, Constants.AmountReturnType.FEE);
                updateBatch(list);
                saveAmountReturn(batchNo, fee1ReturnCustomerNo, new BigDecimal(fee1ReturnRate), returnAmount1, Constants.AmountReturnType.FEE);
                break;
            }
        }
    }

    /**
     * 返还积分数据计算
     *
     * @param batchNo
     */
    private void integralReturn(String batchNo) {
        // 当天积分求和
        Map<String, Object> param = new HashMap<>();
        param.put("beginTime", DateUtil.getBeginDay());
        param.put("endTime", DateUtil.getEndDay());
        BigDecimal totalIntegral = dealOrderService.sumAllIntegral(param);
        // 入库,存在则先删除
        List<AmountReturn> list = amountReturnService.findByBatchNo(batchNo, Constants.AmountReturnType.INTEGRAL);
        updateBatch(list);
        saveAmountReturn(batchNo, integralReturnCustomerNo, new BigDecimal(integralReturnRate), totalIntegral, Constants.AmountReturnType.INTEGRAL);
    }

    /**
     * 积分返还办理
     */
    public ApiResult integralReturnNew() {
        // 判断开关状态
        MatchConfig matchConfig = matchConfigService.getSwitch("integralReturn");
        if (matchConfig == null || "off".equals(matchConfig.getMatchSwitch())) {
            return ApiResult.badParam("积分返还开关已关闭");
        }
        // 计算返还金额
        BigDecimal totalIntegral = BigDecimal.ZERO;
        List<DealOrder> list = dealOrderService.sumIntegral(DateUtil.yyyyMMddWithLine.get().format(new Date()));
        if (CollectionUtils.isEmpty(list)) {
            return ApiResult.badParam("成交单列表为空");
        }
        for (DealOrder dealOrder : list) {
            totalIntegral = totalIntegral.add(dealOrder.getIntegral());
        }
        if (totalIntegral.compareTo(BigDecimal.ZERO) <= 0) {
            return ApiResult.badParam("积分返还金额小于等于0");
        }
        // 数据返还
        ChangeAmountDto changeAmountDto = new ChangeAmountDto();
        changeAmountDto.setCustomerNo(integralReturnCustomerNo);
        StringBuffer sb = new StringBuffer(DateUtil.yyyyMMddNoLine.get().format(new Date()))
                .append("-")
                .append(RandomUtils.getRandomNumber(6));
        changeAmountDto.setOrderNo(sb.toString());
        changeAmountDto.setChangeAmount(totalIntegral);
        changeAmountDto.setTradeType(FundTradeTypeEnum.INTEGRARL_SETTLEMENT.getCode());
        changeAmountDto.setReType(Byte.valueOf("2"));
        // 增加资金流水
        accountService.changeAccount(changeAmountDto);
        // 更新成交单返还状态
        for (DealOrder dealOrder : list) {
            dealOrder.setIntegralReturnFlag("yes");
            dealOrder.setModifyTime(new Date());
            boolean flag = dealOrderService.update(dealOrder);
            if (!flag) {
                throw new RuntimeException("更新成交单返还状态失败");
            }
        }
        return ApiResult.success();
    }

    /**
     * 用户交易提货的货款+积分商城兑换商品消费的积分（与人民币1：1）
     *
     * @param batchNo
     */
    private void creditReturn(String batchNo) {
        Date previousDay = DateUtil.appendHhmmss(tradeDayService.getAddNTradeDay(-1), creditTime);
        Date nowDay = DateUtil.appendHhmmss(new Date(), creditTime);
        // 交易提货款
        BigDecimal deliveryAmount = deliveryService.sumAmount(previousDay, nowDay);
        log.info("提货款为{}", deliveryAmount);
        // 商城积分兑换款
        BigDecimal shopAmount = shpOrderService.sumAmount(previousDay, nowDay);
        log.info("商城积分兑换款为{}", shopAmount);
        BigDecimal total = deliveryAmount.add(shopAmount);
        BigDecimal amount = NumberUtil.roundFloor(total.multiply(new BigDecimal(creditRate)), 2);
        // 入库,存在则先删除
        List<AmountReturn> list = amountReturnService.findByBatchNo(batchNo, Constants.AmountReturnType.CREDIT);
        updateBatch(list);
        saveAmountReturn(batchNo, creditCustomerNo, new BigDecimal(creditRate), amount, Constants.AmountReturnType.CREDIT);
    }


    /**
     * 保存返还记录
     *
     * @param batchNo
     * @param customerNo
     * @param returnRate
     * @param returnAmount
     */
    private void saveAmountReturn(String batchNo, String customerNo, BigDecimal returnRate,
                                  BigDecimal returnAmount, String type) {
        AmountReturn am = new AmountReturn();
        am.setBatchNo(batchNo);
        am.setCustomerNo(customerNo);
        am.setReturnRate(returnRate);
        am.setReturnAmount(returnAmount);
        am.setStatus(Constants.AmountReturnStatus.WAIT);
        am.setType(type);
        amountReturnService.save(am);
    }

    /**
     * 批量删除
     *
     * @param list
     */
    private void updateBatch(List<AmountReturn> list) {
        if (!CollectionUtils.isEmpty(list)) {
            for (AmountReturn item : list) {
                item.setModifyTime(new Date());
                item.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
                amountReturnService.update(item);
            }
        }
    }

    /**
     * 构建清算bean基础信息
     *
     * @param queryId
     * @param custAccountId
     * @param custName
     * @param thirdCustId
     * @param batchNo
     * @return
     */
    private CleanVo buildCleanVoBaseInfo(Integer queryId, String custAccountId, String custName,
                                         String thirdCustId, String batchNo, Byte funcFlag) {
        Date now = new Date();
        CleanVo vo = new CleanVo();
        vo.setQueryId(queryId);
        vo.setTranDateTime(DateUtil.yyyyMMddHHmmssNoLine.get().format(now));
        vo.setCounterId("operator");
        vo.setSupacctId("");
        vo.setFuncCode("");
        vo.setCustAcctId(custAccountId);
        vo.setCustName(custName);
        vo.setThirdCustId(thirdCustId);
        vo.setThirdLogNo(batchNo);
        vo.setCcyCode("");
        vo.setNote("会员日终扎差清算");
        vo.setFuncFlag(funcFlag);
        return vo;
    }
}
