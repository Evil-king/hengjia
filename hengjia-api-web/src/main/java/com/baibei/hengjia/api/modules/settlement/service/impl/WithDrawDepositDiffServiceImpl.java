package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.account.service.IAccountService;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderDepositDto;
import com.baibei.hengjia.api.modules.cash.bean.dto.OrderWithdrawDto;
import com.baibei.hengjia.api.modules.cash.component.SerialNumberComponent;
import com.baibei.hengjia.api.modules.cash.model.OrderDeposit;
import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.cash.model.SigningRecord;
import com.baibei.hengjia.api.modules.cash.service.IOrderDepositService;
import com.baibei.hengjia.api.modules.cash.service.IOrderWithdrawService;
import com.baibei.hengjia.api.modules.cash.service.ISigningRecordService;
import com.baibei.hengjia.api.modules.cash.service.IValidateService;
import com.baibei.hengjia.api.modules.cash.service.impl.CashFunctionDepositServiceImpl;
import com.baibei.hengjia.api.modules.cash.withdrawProsscess.Utils;
import com.baibei.hengjia.api.modules.settlement.bean.dto.DealDiffDto;
import com.baibei.hengjia.api.modules.settlement.bean.dto.WithDrawDepositDto;
import com.baibei.hengjia.api.modules.settlement.dao.WithDrawDepositDiffMapper;
import com.baibei.hengjia.api.modules.settlement.model.BankOrder;
import com.baibei.hengjia.api.modules.settlement.model.WithDrawDepositDiff;
import com.baibei.hengjia.api.modules.settlement.service.IBankOrderService;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.api.modules.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.api.modules.sms.core.PropertiesVal;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.enumeration.FundTradeTypeEnum;
import com.baibei.hengjia.common.tool.enumeration.OrderTypeEnum;
import com.baibei.hengjia.common.tool.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author: Longer
 * @date: 2019/06/25 11:30:00
 * @description: WithDrawDepositDiff服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class WithDrawDepositDiffServiceImpl extends AbstractService<WithDrawDepositDiff> implements IWithDrawDepositDiffService {

    @Autowired
    private IBankOrderService bankOrderService;
    @Autowired
    private IOrderWithdrawService orderWithdrawService;
    @Autowired
    private SerialNumberComponent serialNumberComponent;
    @Autowired
    private IOrderDepositService orderDepositService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IValidateService validateService;
    @Autowired
    private  CashFunctionDepositServiceImpl cashFunctionDepositService;
    @Autowired
    private WithDrawDepositDiffMapper withDrawDepositDiffMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISigningRecordService signingRecordService;
    @Autowired
    private ICleanFlowPathService cleanFlowPathService;
    @Autowired
    private PropertiesVal propertiesVal;


    @Override
    public ApiResult withDrawDepositDiff(String batchNo) {
        log.info("======开始出入金流水对账=======");
        if(StringUtils.isEmpty(batchNo)){
            return ApiResult.error("对账失败，未指定对账批次");
        }
        //先删除，防止多次对账，多次入库的情况
        this.deleteByBatchNo(batchNo);
        /**
         * 对账：
         * 银行出入金流水信息与业务系统出入金流水信息进行对账
         */
        List<BankOrder> bankOrderList = bankOrderService.getOrderListByBatchNo(batchNo);

        //系统出金订单信息
        List<OrderWithdraw> withdrawOrderList = orderWithdrawService.getPeriodOrderList(batchNo);
        //系统入金订单信息
        List<OrderDeposit> depositOrderList = orderDepositService.getPeriodOrderList(batchNo);

        //合并出金和入金流水
        List<WithDrawDepositDto> combineList = combineWithDrawAndDepositList(withdrawOrderList,depositOrderList);

        //生成差异流水集合
        List<WithDrawDepositDiff> withDrawDepositDiffList=diffList(bankOrderList,combineList,batchNo);
        if (!CollectionUtils.isEmpty(withDrawDepositDiffList)) {
            //插入对账流水
            withDrawDepositDiffMapper.insertList(withDrawDepositDiffList);
        }
        /*// 发送开始清算的消息
        redisUtil.leftPush(RedisConstant.SET_CLEAN_PRE_LIST,batchNo);
        redisUtil.pub(RedisConstant.SET_CLEAN_PRE_TOPIC,RedisConstant.SET_CLEAN_PRE_TOPIC);*/
        // 更新清算进度状态
        cleanFlowPathService.findAndUpdate(batchNo, Constants.CleanFlowPathCode.ACCOUNTCHECK_FILE, Constants.CleanFlowPathStatus.COMPLETED);
        log.info("======结束出入金流水对账=======");

        return ApiResult.success();
    }

    @Override
    public ApiResult dealDiff(DealDiffDto dealDiffDto) {
        //根据id获取异常信息
        WithDrawDepositDiff withDrawDepositDiff = withDrawDepositDiffMapper.selectByPrimaryKey(dealDiffDto.getDiffId());
        if (withDrawDepositDiff==null) {
            throw new ServiceException("找不到该差异流水信息");
        }
        String diffType = withDrawDepositDiff.getDiffType();//差异类型
        if(withDrawDepositDiff.getStatus().equals(Constants.DiffStatus.DEAL)){
            throw new ServiceException("调账失败，不可重复调账");
        }
        //调账
        deal (withDrawDepositDiff);
        //改为已处理状态
        withDrawDepositDiff.setStatus(Constants.DiffStatus.DEAL);
        int i = this.updateDiffWithWait(withDrawDepositDiff);
        if(i==0){
            throw new ServiceException("调账失败，异常流水乐观锁问题");
        }
        return ApiResult.success();
    }

    @Override
    public int deleteByBatchNo(String batchNo) {
        Condition condition = new Condition(BankOrder.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!StringUtils.isEmpty(batchNo)){
            criteria.andEqualTo("batchNo",batchNo);
        }
        return withDrawDepositDiffMapper.deleteByCondition(condition);
    }

    @Override
    public int updateDiffWithWait(WithDrawDepositDiff withDrawDepositDiff) {
        Condition condition = new Condition(WithDrawDepositDiff.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("status",Constants.DiffStatus.WAIT);//乐观锁
        criteria.andEqualTo("id",withDrawDepositDiff.getId());
        return withDrawDepositDiffMapper.updateByCondition(withDrawDepositDiff,condition);
    }


    /**
     * 处理差异流水(调账)
     * @param withDrawDepositDiff
     */
    public void deal (WithDrawDepositDiff withDrawDepositDiff){
        String diffType = withDrawDepositDiff.getDiffType();//差异类型
        String type = withDrawDepositDiff.getType();//（withdraw:出金，deposit:入金）
        switch(diffType){
            case Constants.DiffType.LONG_DIFF ://长款差异（银行有，系统没有）
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    withDrawLongDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    depositLongDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.AMOUNT_DIFF ://金额不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    withDrawAmountDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    depositAmountDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.STATUS_DIFF ://状态不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    withDrawStatusDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    depositStatusDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.AMOUNT_STATUS_DIFF://金额和状态不一致
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    withDrawAmountStatusDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    depositAmountStatusDiff(withDrawDepositDiff);
                break;
            case Constants.DiffType.SHORT_DIFF ://短款差异（系统有，银行没有）
                if(OrderTypeEnum.WITHDRAW.getOrderType().equals(type))//出金
                    withDrawShortDiff(withDrawDepositDiff);
                if(OrderTypeEnum.DEPOSIT.getOrderType().equals(type))//入金
                    depositShortDiff(withDrawDepositDiff);
                break;
            default : //可选
                log.info("diffType error ==>{}",diffType);
        }
    }

    /**
     * 出金，长款差异 处理逻辑
     * @param withDrawDepositDiff
     */
    public void withDrawLongDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }
        //生成出金订单
        OrderWithdrawDto orderWithdrawDto = new OrderWithdrawDto();
        orderWithdrawDto.setType("dealDiff");
        orderWithdrawDto.setCustomerNo(customerNo);
        BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
        orderWithdrawDto.setOrderAmt(withdrawAmount);//金额
        orderWithdrawDto.setStatus(Constants.WithDrawStatus.SUCCESS);
        orderWithdrawDto.setBankName(signingRecord.getBankName());
        orderWithdrawDto.setReceiveAccount(signingRecord.getRelatedAcctId());//收款账号
        orderWithdrawDto.setExternalNo(withDrawDepositDiff.getExternalNo());//外部流水号
        validateService.createOrder(orderWithdrawDto);
    }

    /**
     * 出金，金额不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public void withDrawAmountDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        /*SigningRecord signingRecord = orderWithdrawService.getSigningRecord(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }*/
        //查询用户出金信息
        OrderWithdraw orderWithdraw = orderWithdrawService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        OrderWithdraw operateOrder = new OrderWithdraw();
        if (!orderWithdraw.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderWithdraw.getCustomerNo());
        }
        if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())>0){//银行金额>恒价金额
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal shouldFee=fee.subtract(orderWithdraw.getHandelFee());
            BigDecimal shouldWithdrawAmount = withdrawAmount.subtract(orderWithdraw.getOrderamt()).subtract(fee);
            if(shouldFee.compareTo(new BigDecimal("0"))>0){
                //扣除手续费
                accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldFee, orderWithdraw.getOrderNo(),
                        (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode(), (byte) 1);
            }
            if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                //扣除钱
                accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldWithdrawAmount, orderWithdraw.getOrderNo(),
                        (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode(), (byte) 1);
            }
            orderWithdraw.setHandelFee(fee);
        }
        if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())<0){//银行金额<恒价金额
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal shouldFee=orderWithdraw.getHandelFee().subtract(fee);
            BigDecimal shouldWithdrawAmount = orderWithdraw.getOrderamt().subtract((withdrawAmount.subtract(fee)));
            if(shouldFee.compareTo(new BigDecimal("0"))>0){
                //加回之前扣除的手续费
                accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldFee, orderWithdraw.getOrderNo(),
                        (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode(), (byte) 2);
            }
            if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                //加回之前扣除的钱
                accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldWithdrawAmount,
                        orderWithdraw.getOrderNo(),
                        (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode(), (byte) 2);
            }
            orderWithdraw.setHandelFee(fee);
        }
        //更新金额
        orderWithdraw.setOrderamt(withDrawDepositDiff.getBankAmount());
        orderWithdrawService.updateOrderByDiff(orderWithdraw);
    }

    /**
     * 出金，状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public void withDrawStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        /*SigningRecord signingRecord = orderWithdrawService.getSigningRecord(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }*/
        //查询用户出金信息
        OrderWithdraw orderWithdraw = orderWithdrawService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        OrderWithdraw operateOrder = new OrderWithdraw();
        if (!orderWithdraw.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderWithdraw.getCustomerNo());
        }
        if(orderWithdraw.getStatus().equals(Constants.WithDrawStatus.FAIL)||orderWithdraw.getStatus().equals(Constants.WithDrawStatus.APPLY_FAIL)){//出金失败或则提现审核失败
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            //扣除手续费
            accountService.updateWithDraw(orderWithdraw.getCustomerNo(), fee, orderWithdraw.getOrderNo(),
                    (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode(), (byte) 1);
            //扣除钱
            accountService.updateWithDraw(orderWithdraw.getCustomerNo(), withDrawDepositDiff.getBankAmount(), orderWithdraw.getOrderNo(),
                    (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode(), (byte) 1);
            orderWithdraw.setHandelFee(fee);
        }
        //更新状态
        orderWithdraw.setStatus(Constants.WithDrawStatus.SUCCESS);
        orderWithdrawService.updateOrderByDiff(orderWithdraw);
    }

    /**
     * 出金，金额和状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public void withDrawAmountStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        /*//用户签约信息
        SigningRecord signingRecord = orderWithdrawService.getSigningRecord(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }*/
        //查询用户出金信息
        OrderWithdraw orderWithdraw = orderWithdrawService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        OrderWithdraw operateOrder = new OrderWithdraw();
        if (!orderWithdraw.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderWithdraw.getCustomerNo());
        }
        if(!orderWithdraw.getStatus().equals(Constants.WithDrawStatus.FAIL)&&!orderWithdraw.getStatus().equals(Constants.WithDrawStatus.APPLY_FAIL)){
            if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())>0){//银行金额>恒价金额
                BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal shouldFee=fee.subtract(orderWithdraw.getHandelFee());
                BigDecimal shouldWithdrawAmount = withdrawAmount.subtract(orderWithdraw.getOrderamt()).subtract(fee);
                if(shouldFee.compareTo(new BigDecimal("0"))>0){
                    //扣除手续费
                    accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldFee, orderWithdraw.getOrderNo(),
                            (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode(), (byte) 1);
                }
                if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                    //扣除钱
                    accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldWithdrawAmount, orderWithdraw.getOrderNo(),
                            (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode(), (byte) 1);
                }
                orderWithdraw.setHandelFee(fee);
            }
            if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())<0){//银行金额<恒价金额
                BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
                BigDecimal shouldFee=orderWithdraw.getHandelFee().subtract(fee);
                BigDecimal shouldWithdrawAmount = orderWithdraw.getOrderamt().subtract((withdrawAmount.subtract(fee)));
                if(shouldFee.compareTo(new BigDecimal("0"))>0){
                    //加回之前扣除的手续费
                    accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldFee, orderWithdraw.getOrderNo(),
                            (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode(), (byte) 2);
                }
                if(shouldWithdrawAmount.compareTo(new BigDecimal("0"))>0){
                    //加回之前扣除的钱
                    accountService.updateWithDraw(orderWithdraw.getCustomerNo(), shouldWithdrawAmount,
                            orderWithdraw.getOrderNo(),
                            (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode(), (byte) 2);
                }
                orderWithdraw.setHandelFee(fee);
            }
            /*if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())>0){//银行金额>恒价金额
                //差额
                BigDecimal subPrice = withDrawDepositDiff.getBankAmount().subtract(orderWithdraw.getOrderamt());
                operateOrder.setOrderamt(subPrice);
                operateOrder.setOrderNo(orderWithdraw.getOrderNo());
                operateOrder.setCustomerNo(orderWithdraw.getCustomerNo());
                orderWithdrawService.operatorMoney(Constants.WithdrawType.DEDUCTING_MONEY,operateOrder,"diffType");
            }
            if(withDrawDepositDiff.getBankAmount().compareTo(orderWithdraw.getOrderamt())<0){//银行金额<恒价金额
                //差额
                BigDecimal subPrice = orderWithdraw.getOrderamt().subtract(withDrawDepositDiff.getBankAmount());
                operateOrder.setOrderamt(subPrice);
                operateOrder.setOrderNo(orderWithdraw.getOrderNo());
                operateOrder.setCustomerNo(orderWithdraw.getCustomerNo());
                orderWithdrawService.operatorMoney(Constants.WithdrawType.ADD_MONEY,operateOrder,"diffType");
            }*/
        }else{//出金失败或者提现审核失败，恒价系统就没扣用户的钱，这时就直接以银行的出金金额为准，扣除用户的钱
            BigDecimal withdrawAmount = Utils.getWithdrawAmount(withDrawDepositDiff.getBankAmount(), propertiesVal.getRate(), propertiesVal.getFee());
            BigDecimal fee = Utils.getFee(withdrawAmount, propertiesVal.getRate(), propertiesVal.getFee());
            //扣除手续费
            accountService.updateWithDraw(orderWithdraw.getCustomerNo(), fee, orderWithdraw.getOrderNo(),
                    (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_SUB.getCode(), (byte) 1);
            //扣除钱
            accountService.updateWithDraw(orderWithdraw.getCustomerNo(), withDrawDepositDiff.getBankAmount(), orderWithdraw.getOrderNo(),
                    (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode(), (byte) 1);
            orderWithdraw.setHandelFee(fee);
        }
        //更新状态和金额
        orderWithdraw.setStatus(Constants.WithDrawStatus.SUCCESS);
        orderWithdraw.setOrderamt(withDrawDepositDiff.getBankAmount());
        orderWithdrawService.updateOrderByDiff(orderWithdraw);
    }

    public void withDrawShortDiff(WithDrawDepositDiff withDrawDepositDiff){
        if (StringUtils.isEmpty(withDrawDepositDiff.getOrderNo())) {
            throw new ServiceException("短款差异，系统订单号为空");
        }
        OrderWithdraw orderWithdraw = orderWithdrawService.getByOrderNo(withDrawDepositDiff.getOrderNo());
        if(orderWithdraw==null){
            throw new ServiceException("短款差异，找不到该出金订单，订单号为："+withDrawDepositDiff.getOrderNo());
        }
        OrderWithdraw operateOrder = new OrderWithdraw();
        //将提现的金额加回给用户
        if(!orderWithdraw.getStatus().equals(Constants.WithDrawStatus.FAIL)||!orderWithdraw.getStatus().equals(Constants.WithDrawStatus.APPLY_FAIL)){//非提现失败的订单
            //加回之前扣除的钱
            accountService.updateWithDraw(orderWithdraw.getCustomerNo(), orderWithdraw.getOrderamt(),
                    orderWithdraw.getOrderNo(),
                    (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode(), (byte) 2);
            accountService.updateWithDraw(orderWithdraw.getCustomerNo(), orderWithdraw.getHandelFee(),
                    orderWithdraw.getOrderNo(),
                    (byte) FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_FEE_ADD.getCode(), (byte) 2);
        }
        //将状态改成 出金失败状态
        orderWithdraw.setStatus(Constants.WithDrawStatus.APPLY_FAIL);
        orderWithdrawService.updateOrderByDiff(orderWithdraw);
    }


    /**
     * 入金，长款差异 处理逻辑
     * @param withDrawDepositDiff
     */
    public void depositLongDiff(WithDrawDepositDiff withDrawDepositDiff) {
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        SigningRecord signingRecord = signingRecordService.findByCustAcctId(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }
        //生成入金订单
        OrderDepositDto orderDepositDto = new OrderDepositDto();
        //入金金额
        orderDepositDto.setTranAmount(withDrawDepositDiff.getBankAmount());
        //入金账号
        orderDepositDto.setInAcctId(signingRecord.getRelatedAcctId());
        //入金账号名称
        orderDepositDto.setInAcctIdName(signingRecord.getAcctName());
        //币种（人民币）
        orderDepositDto.setCcyCode("RMB");
        //会计日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date acctDate = null;
        try {
            acctDate = simpleDateFormat.parse(customerBankOrder.getPayDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        orderDepositDto.setAcctDate(acctDate);
        //会员子账号
        orderDepositDto.setCustAcctId(signingRecord.getCustAcctId());
        Map parmaKeyDict = new HashMap();
        parmaKeyDict.put("externalNo",withDrawDepositDiff.getExternalNo());
        //生成订单号
        parmaKeyDict.put("ThirdLogNo", serialNumberComponent.generateOrderNo(OrderDeposit.class, orderDepositService, "D", "orderNo"));
        cashFunctionDepositService.doResponse(orderDepositDto, parmaKeyDict);
    }


    /**
     * 入金，金额不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public void depositAmountDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        /*SigningRecord signingRecord = orderWithdrawService.getSigningRecord(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }*/
        //查询用户入金信息
        OrderDeposit orderDeposit = orderDepositService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        if (!orderDeposit.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderDeposit.getCustomerNo());
        }
        if(withDrawDepositDiff.getBankAmount().compareTo(orderDeposit.getOrderAmt())>0){//银行金额>恒价金额
            //差额
            BigDecimal subPrice = withDrawDepositDiff.getBankAmount().subtract(orderDeposit.getOrderAmt());
            accountService.updateWithDraw(customerNo,subPrice,orderDeposit.getOrderNo(),FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode()
                    ,Byte.parseByte("2"));
        }
        if(withDrawDepositDiff.getBankAmount().compareTo(orderDeposit.getOrderAmt())<0){//银行金额<恒价金额
            //差额
            BigDecimal subPrice = orderDeposit.getOrderAmt().subtract(withDrawDepositDiff.getBankAmount());
            accountService.updateWithDraw(customerNo,subPrice,orderDeposit.getOrderNo(),FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode()
                    ,Byte.parseByte("1"));
        }

        //更新金额
        orderDeposit.setOrderAmt(withDrawDepositDiff.getBankAmount());
        orderDepositService.update(orderDeposit);
    }

    /**
     * 入金，状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public void depositStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        /*SigningRecord signingRecord = orderWithdrawService.getSigningRecord(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }*/
        //查询用户入金信息
        OrderDeposit orderDeposit = orderDepositService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        if (!orderDeposit.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderDeposit.getCustomerNo());
        }
        if(!orderDeposit.getStatus().equals(Constants.Status.SUCCESS)){//入金失败
            accountService.updateWithDraw(customerNo,orderDeposit.getOrderAmt(),orderDeposit.getOrderNo(),FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode()
                    ,Byte.parseByte("2"));
        }
        //更新状态
        orderDeposit.setStatus(Constants.Status.SUCCESS);
        orderDepositService.update(orderDeposit);
    }

    /**
     * 入金，金额和状态不一致 处理逻辑
     * @param withDrawDepositDiff
     */
    public void depositAmountStatusDiff(WithDrawDepositDiff withDrawDepositDiff){
        //查询用户信息
        BankOrder bankOrder = new BankOrder();
        bankOrder.setBankSerialNo(withDrawDepositDiff.getExternalNo());
        BankOrder customerBankOrder = bankOrderService.getOneBankOrder(bankOrder);
        String customerNo = customerBankOrder.getMemberNo();
        //用户签约信息
        /*SigningRecord signingRecord = orderWithdrawService.getSigningRecord(customerNo);
        if(signingRecord==null){
            throw new ServiceException("签约用户不存在");
        }*/
        //查询用户入金信息
        OrderDeposit orderDeposit = orderDepositService.getOrderByExternalNo(withDrawDepositDiff.getExternalNo());
        if (!orderDeposit.getCustomerNo().equals(customerNo)) {
            throw new ServiceException("用户信息不对应，银行："+customerNo+" 系统："+orderDeposit.getCustomerNo());
        }

        if(!orderDeposit.getStatus().equals(Constants.Status.SUCCESS)){//入金失败
            accountService.updateWithDraw(customerNo,withDrawDepositDiff.getBankAmount(),orderDeposit.getOrderNo(),FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_ADD.getCode()
                    ,Byte.parseByte("2"));
        }
        //更新订单状态和金额
        orderDeposit.setStatus(Constants.Status.SUCCESS);
        orderDeposit.setOrderAmt(withDrawDepositDiff.getBankAmount());
        orderDepositService.update(orderDeposit);
    }

    /**
     * 入金，短款差错 处理逻辑
     * @param withDrawDepositDiff
     */
    public void depositShortDiff(WithDrawDepositDiff withDrawDepositDiff){
        if (StringUtils.isEmpty(withDrawDepositDiff.getOrderNo())) {
            throw new ServiceException("短款差异，系统订单号为空");
        }
        OrderDeposit orderDeposit = orderDepositService.getOrderByOrderNo(withDrawDepositDiff.getOrderNo());
        if(orderDeposit==null){
            throw new ServiceException("找不到指定的入金订单信息");
        }
        if(orderDeposit.getStatus().equals(Constants.Status.SUCCESS)){//入金成功，扣钱
            accountService.updateWithDraw(orderDeposit.getCustomerNo(),orderDeposit.getOrderAmt(),orderDeposit.getOrderNo(),FundTradeTypeEnum.ACCOUNT_ADJUSTMENT_SUB.getCode()
                    ,Byte.parseByte("1"));
        }
        //将状态更新成失败状态
        orderDeposit.setStatus(Constants.Status.FAIL);
        orderDepositService.update(orderDeposit);
    }


    public WithDrawDepositDiff createDiffInfo(BankOrder bankOrder,WithDrawDepositDto withDrawDepositDto,String diffType,String batchNo){
        WithDrawDepositDiff withDrawDepositDiff = new WithDrawDepositDiff();
        withDrawDepositDiff.setBatchNo(batchNo);
        if(Constants.DiffType.LONG_DIFF.equals(diffType)){//长款差异
            //类型（withdraw=出金，deposit=入金）
            withDrawDepositDiff.setType(bankOrder.getType().toString().equals("1")?OrderTypeEnum.WITHDRAW.getOrderType():OrderTypeEnum.DEPOSIT.getOrderType());
            //对账差异 长款差错（银行有，系统没有）
            withDrawDepositDiff.setDiffType(diffType);
            //银行系统订单号
            withDrawDepositDiff.setExternalNo(bankOrder.getBankSerialNo());
            //银行订单状态（全部是success）
            withDrawDepositDiff.setBankStatus(Constants.Status.SUCCESS);
            //银行系统订单金额
            withDrawDepositDiff.setBankAmount(bankOrder.getAmount());
            //处理状态，wait=待处理，deal=已处理
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setCreateTime(new Date());
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiff.setFlag(Byte.valueOf(Constants.Flag.VALID));
        }else if(Constants.DiffType.SHORT_DIFF.equals(diffType)){//短款差异
            //类型（withdraw=出金，deposit=入金）
            withDrawDepositDiff.setType(withDrawDepositDto.getType());
            //恒价系统订单号
            withDrawDepositDiff.setOrderNo(withDrawDepositDto.getOrderNo());
            //外部订单号
            withDrawDepositDiff.setExternalNo(withDrawDepositDto.getExternalNo());
            //恒价系统订单状态
            withDrawDepositDiff.setHengjiaStatus(withDrawDepositDto.getStatus());
            //恒价系统订单金额
            withDrawDepositDiff.setHengjiaAmount(withDrawDepositDto.getAmount());
            withDrawDepositDiff.setDiffType(diffType);
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setCreateTime(new Date());
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiff.setFlag(Byte.valueOf(Constants.Flag.VALID));
        }else{//其他差异
            //类型（withdraw=出金，deposit=入金）
            withDrawDepositDiff.setType(withDrawDepositDto.getType());
            //对账差异 金额不一致
            withDrawDepositDiff.setDiffType(diffType);
            //恒价系统订单号
            withDrawDepositDiff.setOrderNo(withDrawDepositDto.getOrderNo());
            //银行系统订单号
            withDrawDepositDiff.setExternalNo(bankOrder.getBankSerialNo());
            //恒价状态
            withDrawDepositDiff.setHengjiaStatus(withDrawDepositDto.getStatus());
            //银行订单状态（全部是success）
            withDrawDepositDiff.setBankStatus(Constants.Status.SUCCESS);
            //恒价系统订单金额
            withDrawDepositDiff.setHengjiaAmount(withDrawDepositDto.getAmount());
            //银行系统订单金额
            withDrawDepositDiff.setBankAmount(bankOrder.getAmount());
            //处理状态，wait=待处理，deal=已处理
            withDrawDepositDiff.setStatus(Constants.DiffStatus.WAIT);
            withDrawDepositDiff.setCreateTime(new Date());
            withDrawDepositDiff.setModifyTime(new Date());
            withDrawDepositDiff.setFlag(Byte.valueOf(Constants.Flag.VALID));
        }
        return withDrawDepositDiff;
    }

    /**
     * 合并系统出金流水和入金流水
     * @param withdrawOrderList
     * @param depositOrderList
     * @return
     */
    public List<WithDrawDepositDto> combineWithDrawAndDepositList(List<OrderWithdraw> withdrawOrderList,List<OrderDeposit> depositOrderList){
        List<WithDrawDepositDto> combineList = new ArrayList<>();
        for (OrderWithdraw orderWithdraw : withdrawOrderList) {
            WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto();
            withDrawDepositDto.setType(OrderTypeEnum.WITHDRAW.getOrderType());
            withDrawDepositDto.setOrderNo(orderWithdraw.getOrderNo());
            withDrawDepositDto.setExternalNo(orderWithdraw.getExternalNo());
            withDrawDepositDto.setAmount(orderWithdraw.getOrderamt());
            withDrawDepositDto.setStatus(orderWithdraw.getStatus());
            combineList.add(withDrawDepositDto);
        }
        for (OrderDeposit orderDeposit : depositOrderList) {
            WithDrawDepositDto withDrawDepositDto = new WithDrawDepositDto();
            withDrawDepositDto.setType(OrderTypeEnum.DEPOSIT.getOrderType());
            withDrawDepositDto.setOrderNo(orderDeposit.getOrderNo());
            withDrawDepositDto.setExternalNo(orderDeposit.getExternalNo());
            withDrawDepositDto.setAmount(orderDeposit.getOrderAmt());
            withDrawDepositDto.setStatus(orderDeposit.getStatus());
            combineList.add(withDrawDepositDto);
        }
        return combineList;
    }
    public List<WithDrawDepositDiff> diffList(List<BankOrder> bankOrderList,List<WithDrawDepositDto> combineList,String batchNo){
        //对账流水信息
        List<WithDrawDepositDiff> withDrawDepositDiffList = new ArrayList<>();
        int count=0;
        for (int i = 0; i < bankOrderList.size(); i++) {
            BankOrder bankOrder = bankOrderList.get(i);
            if(combineList.size()!=0){
                for (int j = 0; j < combineList.size(); j++) {
                    WithDrawDepositDto withDrawDepositDto = combineList.get(j);
                    if(!bankOrder.getBankSerialNo().equals(withDrawDepositDto.getExternalNo())){
                        count++;
                        //长款差异，银行有，系统没有
                        if(count==combineList.size()){
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.LONG_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                            count=0;//重置
                        }
                    }else{
                        //金额不一致
                        if (withDrawDepositDto.getAmount().compareTo(bankOrder.getAmount())!=0&&
                                (withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.SUCCESS)||withDrawDepositDto.getStatus().equals(Constants.Status.SUCCESS))) {
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.AMOUNT_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                        }
                        //状态不一致(出金成功状态:4 ，入金成功状态:success)
                        if(withDrawDepositDto.getAmount().compareTo(bankOrder.getAmount())==0
                                &&(!withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.SUCCESS)&&!withDrawDepositDto.getStatus().equals(Constants.Status.SUCCESS))){
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.STATUS_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                        }
                        //金额和状态不一致
                        if(withDrawDepositDto.getAmount().compareTo(bankOrder.getAmount())!=0&&
                                (!withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.SUCCESS)&&!withDrawDepositDto.getStatus().equals(Constants.Status.SUCCESS))){
                            WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,withDrawDepositDto,Constants.DiffType.AMOUNT_STATUS_DIFF,batchNo);
                            withDrawDepositDiffList.add(withDrawDepositDiff);
                        }
                        count=0;//重置
                    }
                }
            }else{ //长款差异，银行有，系统没有
                WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder,null,Constants.DiffType.LONG_DIFF,batchNo);
                withDrawDepositDiffList.add(withDrawDepositDiff);
            }
            count=0;//重置
        }

        //短款差错（系统有，银行没有）
        int countj=0;
        for (int i = 0; i < combineList.size(); i++) {
            WithDrawDepositDto withDrawDepositDto = combineList.get(i);
            if(bankOrderList.size()!=0){
                for (int j = 0; j < bankOrderList.size(); j++) {
                    BankOrder bankOrder = bankOrderList.get(j);
                    if(!withDrawDepositDto.getExternalNo().equals(bankOrder.getBankSerialNo())){
                        countj++;
                        if(countj==bankOrderList.size()){
                            if(!withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.APPLY_FAIL)
                                    &&!withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.FAIL)
                                    &&!withDrawDepositDto.getStatus().equals(Constants.Status.FAIL)) {
                                WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(bankOrder, withDrawDepositDto, Constants.DiffType.SHORT_DIFF, batchNo);
                                withDrawDepositDiffList.add(withDrawDepositDiff);
                            }
                        }
                    }
                }
            }else{
                if(!withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.APPLY_FAIL)
                        &&!withDrawDepositDto.getStatus().equals(Constants.WithDrawStatus.FAIL)
                &&!withDrawDepositDto.getStatus().equals(Constants.Status.FAIL)){
                    WithDrawDepositDiff withDrawDepositDiff = createDiffInfo(null,withDrawDepositDto,Constants.DiffType.SHORT_DIFF,batchNo);
                    withDrawDepositDiffList.add(withDrawDepositDiff);
                }
            }
            countj=0;//重置
        }
        return withDrawDepositDiffList;
    }
}
