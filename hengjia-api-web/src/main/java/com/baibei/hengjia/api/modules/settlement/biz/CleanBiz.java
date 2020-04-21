package com.baibei.hengjia.api.modules.settlement.biz;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.account.service.IRecordFreezingAmountService;
import com.baibei.hengjia.api.modules.cash.component.PABDocumentUtils;
import com.baibei.hengjia.api.modules.cash.enumeration.CashFunctionType;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.ICashFunctionService;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.settlement.bean.dto.BeginCleanDto;
import com.baibei.hengjia.api.modules.settlement.bean.vo.*;
import com.baibei.hengjia.api.modules.settlement.model.*;
import com.baibei.hengjia.api.modules.settlement.service.*;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.api.modules.trade.service.IDealOrderService;
import com.baibei.hengjia.api.modules.trade.service.IMatchLogService;
import com.baibei.hengjia.api.modules.trade.service.ITradeDayService;
import com.baibei.hengjia.api.modules.utils.SFTPUtils;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import com.baibei.hengjia.common.tool.utils.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/24 9:54 AM
 * @description: 清算业务逻辑实现
 */
@Component
@Slf4j
public class CleanBiz {
    // 资金汇总账号
    @Value("${cash.supAcctId}")
    private String supAcctId;
    // FTP路径
    @Value("${ftp.path}")
    private String ftpPath;
    // 交易手续费计提待返比例
    @Value("${jt.rate}")
    private String jtRate;
    // 手续费返还1比例
    @Value("${fee1.return.rate}")
    private String fee1ReturnRate;
    // 手续费返还1交易商账号
    @Value("${fee1.return.customerno}")
    private String fee1ReturnCustomerNo;
    // 盈利转积分结转账户
    @Value("${integral.return.customerno}")
    private String integralReturnCustomerNo;

    @Value("${settlement.credit.customerNo}")
    private String creditCustomerNo;
    /**
     * 挂牌商
     */
    @Value("${hengjia.lister}")
    private String hengjiaLister;

    /**
     * 经销商
     */
    @Value("${distributor.customerNo}")
    private String distributorCustomerNo;

    // 功能代码,30为扎差清算
    private final String FuncCode = "30";
    // 币种
    private final String CcyCode = "RMB";

    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IDealOrderService dealOrderService;
    @Autowired
    private ICleanDataService cleanDataService;
    @Autowired
    private ICleanLogService cleanLogService;
    @Autowired
    PropertiesVal propertiesVal;
    @Autowired
    private List<ICashFunctionService> cashFunctionServiceList;
    @Autowired
    private ITradeDayService tradeDayService;
    @Autowired
    private SFTPUtils sftpUtils;
    @Autowired
    private PABDocumentUtils pabDocumentUtils;
    @Autowired
    private IRecordFreezingAmountService recordFreezingAmountService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private IMatchLogService matchLogService;
    private static final BigDecimal oneHundred = new BigDecimal("100");
    @Autowired
    private IOldBalanceService oldBalanceService;
    @Autowired
    private ICustDzFailService custDzFailService;
    @Autowired
    private IBatFailResultService batFailResultService;
    @Autowired
    private ICleanProgressService cleanProgressService;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;
    @Autowired
    private IAmountReturnService amountReturnService;
    @Autowired
    private ICleanHelpService cleanHelpService;


    /**
     * 生成清算数据
     *
     * @param batchNo
     */
    public void generateCleanData(String batchNo) {
        log.info("开始生成清算数据,batchNo={}", batchNo);
        long start = System.currentTimeMillis();
        /*// step1.判断今日是否为交易日
        Date tradeDay;
        try {
            tradeDay = DateUtil.yyyyMMddNoLine.get().parse(batchNo);
        } catch (ParseException e) {
            throw new ServiceException("batchNo格式错误");
        }
        boolean isTradeDay = tradeDayService.isTradeDay(tradeDay);
        if (!isTradeDay) {
            throw new ServiceException("非交易，不能进行清算");
        }*/
        // step2.清算数据统计,如果批次号已经存在,则先删除
        List<CleanData> existCleanDataList = cleanDataService.findByBatchNo(batchNo);
        if (!CollectionUtils.isEmpty(existCleanDataList)) {
            List<Long> idList = new ArrayList<>();
            for (CleanData cleanData : existCleanDataList) {
                idList.add(cleanData.getId());
            }
            cleanDataService.softDelete(idList);
        }
        // step3.清算数据统计
        List<CleanVo> cleanVoList = statistics(batchNo);
        if (CollectionUtils.isEmpty(cleanVoList)) {
            log.info("batchNo={}清算统计数据为空", batchNo);
            return;
        }
        // step4.清算数据统计结果入库
        List<CleanData> cleanDataList = BeanUtil.copyProperties(cleanVoList, CleanData.class);
        Date now = new Date();
        for (CleanData cleanData : cleanDataList) {
            cleanData.setBatchNo(batchNo);
            cleanData.setCreateTime(now);
            cleanData.setModifyTime(now);
            cleanData.setFlag(Byte.valueOf(Constants.Flag.VALID));
            cleanData.setCleanStatus(Constants.CleanStatus.WAIT);
        }
        cleanDataService.save(cleanDataList);
        // step5.更新清算流程
        cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.CLEAN_FILE, Constants.CleanFlowPathStatus.COMPLETED);
        log.info("生成清算数据结束,耗时={}", System.currentTimeMillis() - start);
    }

    /**
     * 生成清算文件，并发送至银行
     *
     * @param batchNo
     */
    public void launchClean(String batchNo) {
        log.info("开始生成清算文件并发送至银行，batchNo={}", batchNo);
        long start = System.currentTimeMillis();
        // step1.查询清算数据
        List<CleanData> cleanDataList = cleanDataService.findByBatchNo(batchNo);
        if (CollectionUtils.isEmpty(cleanDataList)) {
            throw new ServiceException("batchNo为{}未生成清算数据");
        }
        // step2.生成清算文件并加密
        Map<String, String> fileResult = generateCleanFile(cleanDataList);
        String fileName = fileResult.get("fileName");
        String password;
        try {
            password = pabDocumentUtils.FileEncrypt(ftpPath + "/" + fileName);
        } catch (Exception e) {
            log.error("文件加密异常", e);
            throw new ServiceException("文件加密异常");
        }
        String encFileName = new StringBuffer(ftpPath).append("/").append(fileResult.get("fileName")).append(".enc").toString();
        // step3.上传FTP
        sftpUtils.upload(encFileName);
        // step4.生成发起清算请求数据信息入库
        CleanLog cleanLog = saveCleanLog(cleanDataList, batchNo, fileName, fileResult.get("fileSize"), password);
        // step5.调用银行1003接口发起清算
        ICashFunctionService cashFunctionService = cashFunctionServiceList.stream()
                .filter(function -> function.getType().getIndex() == CashFunctionType.BEGIN_CLEAN.getIndex())
                .findFirst().orElse(null);
        BeginCleanDto cleanDto = BeanUtil.copyProperties(cleanLog, BeginCleanDto.class);
        cleanDto.setThirdLogNo(batchNo);
        ApiResult apiResult = cashFunctionService.request(cleanDto);
        // step6.更新清算日志状态
        if (!apiResult.hasSuccess()) {
            log.info("发送清算请求至银行失败,apiResult={}", JSON.toJSONString(apiResult));
            cleanLogService.updateByBatchNo(batchNo, Constants.CleanLogStatus.FAIL, "");
            throw new ServiceException("发送清算文件至银行失败");
        }
        BeginCleanVo cleanVo = (BeginCleanVo) apiResult.getData();
        cleanLogService.updateByBatchNo(batchNo, Constants.CleanLogStatus.SUCCESS, JSON.toJSONString(cleanVo));
        // step7.更新清算流程
        cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.LAUNCH_CLEAN, Constants.CleanFlowPathStatus.COMPLETED);
        log.info("生成清算文件并发送至银行结束，耗时={}", (System.currentTimeMillis() - start));
    }


    /**
     * 清算数据统计
     *
     * @param batchNo
     * @return
     */
    private List<CleanVo> statistics(String batchNo) {
        long start = System.currentTimeMillis();
        Date tradeDay;
        try {
            tradeDay = DateUtil.yyyyMMddNoLine.get().parse(batchNo);
        } catch (ParseException e) {
            throw new ServiceException("batchNo格式错误");
        }
        // step1.查询系统签约存管用户,清算要求全量对账
        List<SigningRecord> signingRecordList = signingRecordService.allList();
        if (CollectionUtils.isEmpty(signingRecordList)) {
            log.info("签约用户为空,不予清算");
            return null;
        }
        List<CleanVo> cleanVoList = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger(1);
        // step2.构建清算bean
        cleanVoList.addAll(signingRecordList.stream().map(signingRecord -> buildCleanVoBaseInfo(atomicInteger.getAndIncrement(), signingRecord.getCustAcctId(),
                signingRecord.getCustName(), signingRecord.getThirdCustId(), batchNo, signingRecord.getFuncFlag())).collect(Collectors.toList()));
        // step2.获取用户当前可用余额/冻结余额
        List<AccountInfo> accountInfoList = accountService.allAccountList();
        for (CleanVo cleanVo : cleanVoList) {
            for (AccountInfo accountInfo : accountInfoList) {
                if (cleanVo.getThirdCustId().equals(accountInfo.getCustomerNo())) {
                    cleanVo.setNewBalance(accountInfo.getBalance());
                    cleanVo.setNewFreezeAmount(accountInfo.getFreezingAmount());
                    break;
                }
            }
        }
        // step3.获取用户当天总冻结金额,总解冻金额
        Map<String, Object> param = new HashMap<>();
        param.put("beginTime", DateUtil.getBeginDay(tradeDay));
        param.put("endTime", DateUtil.getEndDay(tradeDay));
        List<CustomerFrozenVo> frozenList = recordFreezingAmountService.sumCustomerFrozenList(param);
        for (CustomerFrozenVo customerFrozenVo : frozenList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equalsIgnoreCase(customerFrozenVo.getCustomerNo())) {
                    cleanVo.setFreezeAmount(customerFrozenVo.getFrozenTotal());
                    cleanVo.setUnfreezeAmount(customerFrozenVo.getUnfrozenTotal().abs());
                    break;
                }
            }
        }
        // step4.成交当天的总贷款,恒为0
        for (CleanVo cleanVo : cleanVoList) {
            cleanVo.setAddTranAmount(BigDecimal.ZERO);
            cleanVo.setCutTranAmount(BigDecimal.ZERO);
        }
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
        BigDecimal matchFee = BigDecimal.ZERO;
        for (CleanVo vo : cleanVoList) {
            matchFee = matchFee.add(vo.getTotalFee());
        }
        // step6.获取当天应支付给交易网的交易手续费总额,公式:当天的手续费合计*（1-计提待返比例）（保留两位小数后，截断）
        for (CleanVo cleanVo : cleanVoList) {
            if (cleanVo.getTotalFee().compareTo(BigDecimal.ZERO) > 0) {
                cleanVo.setTranHandFee(NumberUtil.roundFloor(cleanVo.getTotalFee().multiply((new BigDecimal("1").
                        subtract(new BigDecimal(jtRate).divide(oneHundred)))), 2));
            }
        }
        // step7.获取亏损总金额
        // ∑（买入数量*买入价格）+当天手续费合计（余额） 注：手续费余额=买入手续费+卖出手续费+提现手续费-交易所分摊；（剔除交易所已经收取的）
        List<CustomerCountVo> lossList = dealOrderService.sumLossAmountForBuy(param);
        for (CustomerCountVo customerCountVo : lossList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo())) {
                    cleanVo.setLossAmount(customerCountVo.getTotal());
                    break;
                }
            }
        }
        // 配票买入也要计算入亏损
        List<CustomerCountVo> matchLossList = matchLogService.sumLoss(param);
        for (CustomerCountVo customerCountVo : matchLossList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo().trim())) {
                    cleanVo.setLossAmount(cleanVo.getLossAmount().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }
        // 当天手续费余额记入亏损
        for (CleanVo cleanVo : cleanVoList) {
            cleanVo.setLossAmount(cleanVo.getLossAmount().add((cleanVo.getTotalFee().subtract(cleanVo.getTranHandFee()))));
        }
        // 清算规则外的亏损
        List<CustomerCountVo> cleanHelpLossList = cleanHelpService.sumLoss(param);
        for (CustomerCountVo customerCountVo : cleanHelpLossList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo().trim())) {
                    cleanVo.setLossAmount(cleanVo.getLossAmount().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }
        // 红木基金算入客户亏损
        List<CustomerCountVo> hongmuFund = dealOrderService.sumHongmuFund(param);
        for (CustomerCountVo customerCountVo : hongmuFund) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo().trim())) {
                    cleanVo.setLossAmount(cleanVo.getLossAmount().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }

        // step8.获取盈利总金额
        //交易用户：∑（卖出数量*卖出价格）-采购专款。
        //返还用户：∑（卖出数量*卖出价格）+采购专款+手续费返还金额。
        //注：手续费返还金额=当天总手续费合计*返还比例
        List<CustomerCountVo> profitList = dealOrderService.sumProfitAmountForSell(param);
        for (CustomerCountVo customerCountVo : profitList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo())) {
                    cleanVo.setProfitAmount(customerCountVo.getTotal());
                    break;
                }
            }
        }
        // 清算规则外的盈利
        List<CustomerCountVo> cleanHelpProfitList = cleanHelpService.sumProfit(param);
        for (CustomerCountVo customerCountVo : cleanHelpProfitList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo().trim())) {
                    cleanVo.setProfitAmount(cleanVo.getProfitAmount().add(customerCountVo.getTotal()));
                    break;
                }
            }
        }
        // 存管上线,系统老用户清算数据处理
        for (CleanVo cleanVo : cleanVoList) {
            OldBalance oldBalance = oldBalanceService.findByCustomerNo(cleanVo.getThirdCustId());
            if (oldBalance != null) {
                log.info("老用户数据清算处理,customerNo={},profitAmount={},balance={}", cleanVo.getThirdCustId(),
                        cleanVo.getProfitAmount(), oldBalance.getBalance());
                // 加上系统余额
                cleanVo.setProfitAmount(cleanVo.getProfitAmount().add(oldBalance.getBalance()));
                // 更新状态
                boolean flag = oldBalanceService.softDelete(oldBalance);
                log.info("更新老用户处理状态成功,customerNo={},flag={}", cleanVo.getThirdCustId(), flag);
            }
        }
        // 全盘采购专款积分总和
        BigDecimal totalIntegral = dealOrderService.sumAllIntegral(param);
        BigDecimal couponAmount = dealOrderService.sumAllCouponAmount(param);
        BigDecimal hongmuFundAmount = dealOrderService.sumAllHongmuFund(param);

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
                cleanVo.setProfitAmount(cleanVo.getProfitAmount().
                        add(totalFee.subtract(totalTranHandFee)));
                break;
            }
        }
        // 计算积分返还用户的盈利总金额,返还用户：∑（卖出数量*卖出价格）-采购专款（自己）+采购专款（全盘）+提货款
        for (CleanVo cleanVo : cleanVoList) {
            if (cleanVo.getThirdCustId().equals(integralReturnCustomerNo)) {
                cleanVo.setProfitAmount(cleanVo.getProfitAmount().add(totalIntegral).add(couponAmount));
                break;
            }
        }
        // 经销商盈利总金额加上红木基金
        for (CleanVo cleanVo : cleanVoList) {
            if (cleanVo.getThirdCustId().equals(distributorCustomerNo)) {
                cleanVo.setProfitAmount(cleanVo.getProfitAmount().add(hongmuFundAmount));
                break;
            }
        }
        // 配票成本算入挂牌商的盈利
        for (CleanVo item : cleanVoList) {
            if (hengjiaLister.equals(item.getThirdCustId())) {
                BigDecimal totalCost = matchLogService.sumCost(param);
                item.setProfitAmount(item.getProfitAmount().add(totalCost));
                log.info("挂牌商配票收入={},总盈利={}", totalCost, item.getProfitAmount());
                break;
            }
        }
        // step9.获取当天交易总笔数
        List<CustomerCountVo> tranCountList = dealOrderService.tradeCount(param);
        for (CustomerCountVo customerCountVo : tranCountList) {
            for (CleanVo cleanVo : cleanVoList) {
                if (cleanVo.getThirdCustId().equals(customerCountVo.getCustomerNo())) {
                    cleanVo.setTranCount(customerCountVo.getTotal() == null ? BigDecimal.ZERO : customerCountVo.getTotal());
                    break;
                }
            }
        }
        // step10.提货贷款业务办理数据清分
        List<AmountReturn> amountReturnList = amountReturnService.findByBatchNo(batchNo, Constants.AmountReturnType.CREDIT);
        if (!CollectionUtils.isEmpty(amountReturnList)) {
            AmountReturn amountReturn = amountReturnList.get(0);
            for (CleanVo item : cleanVoList) {
                if (hengjiaLister.equals(item.getThirdCustId())) {
                    item.setLossAmount(item.getLossAmount().add(amountReturn.getReturnAmount()));
                }
                if (creditCustomerNo.equals(item.getThirdCustId())) {
                    item.setProfitAmount(item.getProfitAmount().add(amountReturn.getReturnAmount()));
                }
            }
        }
        // step11.剔除解约用户
        List<CleanVo> unvalidList = new ArrayList<>();
        for (CleanVo vo : cleanVoList) {
            if (vo.getFuncFlag().intValue() == 3) {
                unvalidList.add(vo);
            }
        }
        if (!CollectionUtils.isEmpty(unvalidList)) {
            cleanVoList.removeAll(unvalidList);
        }
        log.info("清算数据统计结束,耗时 {} ms", System.currentTimeMillis() - start);
        return cleanVoList;
    }


    /**
     * 更新清算状态
     *
     * @param batchNo
     */
    public void updateCleanStatus(String batchNo) {
        List<String> failCustomerNoList = new ArrayList<>();
        // 筛选清算失败的客户列表
        List<BatFailResult> batFailResultList = batFailResultService.findByBatchNo(batchNo);
        if (!CollectionUtils.isEmpty(batFailResultList)) {
            for (BatFailResult item : batFailResultList) {
                failCustomerNoList.add(item.getThirdCustId());
            }
        }
        // 筛选清算不平的客户列表
        List<CustDzFail> custDzFailList = custDzFailService.findByBatchNo(batchNo);
        if (!CollectionUtils.isEmpty(custDzFailList)) {
            for (CustDzFail item : custDzFailList) {
                failCustomerNoList.add(item.getThirdCustId());
            }
        }
        // 更新清算失败
        cleanDataService.updateCleanFail(batchNo, failCustomerNoList);
        // 剩余客户更新为清算成功
        cleanDataService.updateCleanSuccess(batchNo, failCustomerNoList);
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
        vo.setSupacctId(supAcctId);
        vo.setFuncCode(FuncCode);
        vo.setCustAcctId(custAccountId);
        vo.setCustName(custName);
        vo.setThirdCustId(thirdCustId);
        vo.setThirdLogNo(batchNo);
        vo.setCcyCode(CcyCode);
        vo.setNote("会员日终扎差清算");
        vo.setFuncFlag(funcFlag);
        return vo;
    }

    /**
     * 写入文件
     *
     * @param list
     */
    private Map<String, String> generateCleanFile(List<CleanData> list) {

        Map<String, String> result = new HashMap<>();
        // BatQs+交易网代码（4位）+时间（14位）.txt
        String fileName = new StringBuffer("BatQs").append(propertiesVal.getQydm()).append(DateUtil.yyyyMMddHHmmssNoLine.get().format(new Date())).append(".txt").toString();
        try {
            File writeName = new File(ftpPath + "/" + fileName);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeName), "GBK"));
            // 写第一行
            bw.write(list.size() + "");
            bw.write("\r\n"); // \r\n即为换行
            StringBuffer sb = new StringBuffer();
            for (CleanData cleanVo : list) {
                sb.append(cleanVo.getQueryId()).append("&");
                sb.append(cleanVo.getTranDateTime()).append("&");
                sb.append(cleanVo.getCounterId()).append("&");
                sb.append(cleanVo.getSupacctId()).append("&");
                sb.append(cleanVo.getFuncCode()).append("&");
                sb.append(cleanVo.getCustAcctId()).append("&");
                sb.append(cleanVo.getCustName()).append("&");
                sb.append(cleanVo.getThirdCustId()).append("&");
                sb.append(cleanVo.getThirdLogNo()).append("&");
                sb.append(cleanVo.getCcyCode()).append("&");
                sb.append(cleanVo.getFreezeAmount()).append("&");
                sb.append(cleanVo.getUnfreezeAmount()).append("&");
                sb.append(cleanVo.getAddTranAmount()).append("&");
                sb.append(cleanVo.getCutTranAmount()).append("&");
                sb.append(cleanVo.getProfitAmount()).append("&");
                sb.append(cleanVo.getLossAmount()).append("&");
                sb.append(cleanVo.getTranHandFee()).append("&");
                sb.append(cleanVo.getTranCount()).append("&");
                sb.append(cleanVo.getNewBalance()).append("&");
                sb.append(cleanVo.getNewFreezeAmount()).append("&");
                sb.append(cleanVo.getNote()).append("&");
                sb.append("").append("&");
                bw.write(sb.toString());
                bw.write("\r\n"); // \r\n即为换行
                sb.setLength(0);
            }
            bw.flush(); // 把缓存区内容压入文件
            result.put("fileSize", writeName.length() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("fileName", fileName);
        return result;
    }


    /**
     * 保存清算日志数据
     *
     * @param cleanVoList
     * @param batchNo
     * @param fileName
     * @param fileSize
     * @param password
     */
    private CleanLog saveCleanLog(List<CleanData> cleanVoList, String batchNo, String fileName, String fileSize, String password) {
        // 存在则先删除
        CleanLog existCleanLog = cleanLogService.findByBatchNo(batchNo);
        if (existCleanLog != null) {
            existCleanLog.setFlag(Byte.valueOf(Constants.Flag.UNVALID));
            existCleanLog.setModifyTime(new Date());
            cleanLogService.update(existCleanLog);
        }
        BigDecimal cutTranAmount = BigDecimal.ZERO;
        BigDecimal addTranAmount = BigDecimal.ZERO;
        BigDecimal profitAmount = BigDecimal.ZERO;
        BigDecimal lossAmount = BigDecimal.ZERO;
        BigDecimal freezeAmount = BigDecimal.ZERO;
        BigDecimal unfreezeAmount = BigDecimal.ZERO;
        for (CleanData cleanVo : cleanVoList) {
            freezeAmount = freezeAmount.add(cleanVo.getFreezeAmount());
            unfreezeAmount = unfreezeAmount.add(cleanVo.getUnfreezeAmount());
            cutTranAmount = cutTranAmount.add(cleanVo.getCutTranAmount());
            addTranAmount = addTranAmount.add(cleanVo.getAddTranAmount());
            profitAmount = profitAmount.add(cleanVo.getProfitAmount());
            lossAmount = lossAmount.add(cleanVo.getLossAmount());
        }
        CleanLog cleanLog = new CleanLog();
        cleanLog.setFuncFlag("1");  // 1表示清算
        cleanLog.setFileName(fileName);
        cleanLog.setFileSize(fileSize);
        cleanLog.setSupAcctId(supAcctId);
        cleanLog.setFreezeAmount(freezeAmount.abs());
        cleanLog.setUnfreezeAmount(unfreezeAmount.abs());
        cleanLog.setQsZcAmount(cutTranAmount.subtract(addTranAmount));
        cleanLog.setSyZcAmount(profitAmount.subtract(lossAmount));
        cleanLog.setReserve(password);
        cleanLog.setStatus(Constants.CleanLogStatus.WAIT);
        cleanLog.setBatchNo(batchNo);
        cleanLog.setCreateTime(new Date());
        cleanLog.setModifyTime(new Date());
        cleanLog.setFlag(Byte.valueOf(Constants.Flag.VALID));
        cleanLogService.save(cleanLog);
        return cleanLog;
    }
}
