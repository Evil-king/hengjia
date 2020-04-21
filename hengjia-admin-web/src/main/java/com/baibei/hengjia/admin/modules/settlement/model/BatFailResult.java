package com.baibei.hengjia.admin.modules.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_bat_fail_result")
public class BatFailResult {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次号
     */
    @Column(name = "batch_no")
    private String batchNo;

    /**
     * 序号
     */
    @Column(name = "query_id")
    private Integer queryId;

    /**
     * 交易时间
     */
    @Column(name = "tran_date_time")
    private String tranDateTime;

    /**
     * 操作员号
     */
    @Column(name = "counter_id")
    private String counterId;

    /**
     * 资金汇总账号
     */
    @Column(name = "supAcct_id")
    private String supacctId;

    /**
     * 功能代码
     */
    @Column(name = "func_code")
    private String funcCode;

    /**
     * 子账户账号
     */
    @Column(name = "cust_acct_id")
    private String custAcctId;

    /**
     * 子账户名称
     */
    @Column(name = "cust_name")
    private String custName;

    /**
     * 交易网会员代码
     */
    @Column(name = "third_cust_id")
    private String thirdCustId;

    /**
     * 交易网流水号
     */
    @Column(name = "third_log_No")
    private String thirdLogNo;

    /**
     * 币种
     */
    @Column(name = "ccy_code")
    private String ccyCode;

    /**
     * 当天总冻结金额
     */
    @Column(name = "freeze_amount")
    private BigDecimal freezeAmount;

    /**
     * 当天总解冻金额
     */
    @Column(name = "unfreeze_amount")
    private BigDecimal unfreezeAmount;

    /**
     * 当天成交的总货款（作为卖方）（增加银行可用余额）
     */
    @Column(name = "addTran_amount")
    private BigDecimal addtranAmount;

    /**
     * 当天成交的总货款（作为买方）（减少银行可用余额）
     */
    @Column(name = "cutTran_amount")
    private BigDecimal cuttranAmount;

    /**
     * 盈利总金额
     */
    @Column(name = "profit_amount")
    private BigDecimal profitAmount;

    /**
     * 亏损总金额
     */
    @Column(name = "loss_amount")
    private BigDecimal lossAmount;

    /**
     * 当天应支付给交易网的交易手续费总额
     */
    @Column(name = "tran_hand_fee")
    private BigDecimal tranHandFee;

    /**
     * 当天交易总笔数
     */
    @Column(name = "tran_count")
    private BigDecimal tranCount;

    /**
     * 交易网端会员最新的可用余额
     */
    @Column(name = "new_balance")
    private BigDecimal newBalance;

    /**
     * 交易网端会员最新的冻结余额
     */
    @Column(name = "new_freeze_amount")
    private BigDecimal newFreezeAmount;

    /**
     * 说明
     */
    private String note;

    /**
     * 保留域
     */
    private String reserve;

    /**
     * 失败错误码
     */
    @Column(name = "error_code")
    private String errorCode;

    /**
     * 失败原因
     */
    @Column(name = "fail_reason")
    private String failReason;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取批次号
     *
     * @return batch_no - 批次号
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 设置批次号
     *
     * @param batchNo 批次号
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 获取序号
     *
     * @return query_id - 序号
     */
    public Integer getQueryId() {
        return queryId;
    }

    /**
     * 设置序号
     *
     * @param queryId 序号
     */
    public void setQueryId(Integer queryId) {
        this.queryId = queryId;
    }

    /**
     * 获取交易时间
     *
     * @return tran_date_time - 交易时间
     */
    public String getTranDateTime() {
        return tranDateTime;
    }

    /**
     * 设置交易时间
     *
     * @param tranDateTime 交易时间
     */
    public void setTranDateTime(String tranDateTime) {
        this.tranDateTime = tranDateTime;
    }

    /**
     * 获取操作员号
     *
     * @return counter_id - 操作员号
     */
    public String getCounterId() {
        return counterId;
    }

    /**
     * 设置操作员号
     *
     * @param counterId 操作员号
     */
    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    /**
     * 获取资金汇总账号
     *
     * @return supAcct_id - 资金汇总账号
     */
    public String getSupacctId() {
        return supacctId;
    }

    /**
     * 设置资金汇总账号
     *
     * @param supacctId 资金汇总账号
     */
    public void setSupacctId(String supacctId) {
        this.supacctId = supacctId;
    }

    /**
     * 获取功能代码
     *
     * @return func_code - 功能代码
     */
    public String getFuncCode() {
        return funcCode;
    }

    /**
     * 设置功能代码
     *
     * @param funcCode 功能代码
     */
    public void setFuncCode(String funcCode) {
        this.funcCode = funcCode;
    }

    /**
     * 获取子账户账号
     *
     * @return cust_acct_id - 子账户账号
     */
    public String getCustAcctId() {
        return custAcctId;
    }

    /**
     * 设置子账户账号
     *
     * @param custAcctId 子账户账号
     */
    public void setCustAcctId(String custAcctId) {
        this.custAcctId = custAcctId;
    }

    /**
     * 获取子账户名称
     *
     * @return cust_name - 子账户名称
     */
    public String getCustName() {
        return custName;
    }

    /**
     * 设置子账户名称
     *
     * @param custName 子账户名称
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * 获取交易网会员代码
     *
     * @return third_cust_id - 交易网会员代码
     */
    public String getThirdCustId() {
        return thirdCustId;
    }

    /**
     * 设置交易网会员代码
     *
     * @param thirdCustId 交易网会员代码
     */
    public void setThirdCustId(String thirdCustId) {
        this.thirdCustId = thirdCustId;
    }

    /**
     * 获取交易网流水号
     *
     * @return third_log_No - 交易网流水号
     */
    public String getThirdLogNo() {
        return thirdLogNo;
    }

    /**
     * 设置交易网流水号
     *
     * @param thirdLogNo 交易网流水号
     */
    public void setThirdLogNo(String thirdLogNo) {
        this.thirdLogNo = thirdLogNo;
    }

    /**
     * 获取币种
     *
     * @return ccy_code - 币种
     */
    public String getCcyCode() {
        return ccyCode;
    }

    /**
     * 设置币种
     *
     * @param ccyCode 币种
     */
    public void setCcyCode(String ccyCode) {
        this.ccyCode = ccyCode;
    }

    /**
     * 获取当天总冻结金额
     *
     * @return freeze_amount - 当天总冻结金额
     */
    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * 设置当天总冻结金额
     *
     * @param freezeAmount 当天总冻结金额
     */
    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    /**
     * 获取当天总解冻金额
     *
     * @return unfreeze_amount - 当天总解冻金额
     */
    public BigDecimal getUnfreezeAmount() {
        return unfreezeAmount;
    }

    /**
     * 设置当天总解冻金额
     *
     * @param unfreezeAmount 当天总解冻金额
     */
    public void setUnfreezeAmount(BigDecimal unfreezeAmount) {
        this.unfreezeAmount = unfreezeAmount;
    }

    /**
     * 获取当天成交的总货款（作为卖方）（增加银行可用余额）
     *
     * @return addTran_amount - 当天成交的总货款（作为卖方）（增加银行可用余额）
     */
    public BigDecimal getAddtranAmount() {
        return addtranAmount;
    }

    /**
     * 设置当天成交的总货款（作为卖方）（增加银行可用余额）
     *
     * @param addtranAmount 当天成交的总货款（作为卖方）（增加银行可用余额）
     */
    public void setAddtranAmount(BigDecimal addtranAmount) {
        this.addtranAmount = addtranAmount;
    }

    /**
     * 获取当天成交的总货款（作为买方）（减少银行可用余额）
     *
     * @return cutTran_amount - 当天成交的总货款（作为买方）（减少银行可用余额）
     */
    public BigDecimal getCuttranAmount() {
        return cuttranAmount;
    }

    /**
     * 设置当天成交的总货款（作为买方）（减少银行可用余额）
     *
     * @param cuttranAmount 当天成交的总货款（作为买方）（减少银行可用余额）
     */
    public void setCuttranAmount(BigDecimal cuttranAmount) {
        this.cuttranAmount = cuttranAmount;
    }

    /**
     * 获取盈利总金额
     *
     * @return profit_amount - 盈利总金额
     */
    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    /**
     * 设置盈利总金额
     *
     * @param profitAmount 盈利总金额
     */
    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    /**
     * 获取亏损总金额
     *
     * @return loss_amount - 亏损总金额
     */
    public BigDecimal getLossAmount() {
        return lossAmount;
    }

    /**
     * 设置亏损总金额
     *
     * @param lossAmount 亏损总金额
     */
    public void setLossAmount(BigDecimal lossAmount) {
        this.lossAmount = lossAmount;
    }

    /**
     * 获取当天应支付给交易网的交易手续费总额
     *
     * @return tran_hand_fee - 当天应支付给交易网的交易手续费总额
     */
    public BigDecimal getTranHandFee() {
        return tranHandFee;
    }

    /**
     * 设置当天应支付给交易网的交易手续费总额
     *
     * @param tranHandFee 当天应支付给交易网的交易手续费总额
     */
    public void setTranHandFee(BigDecimal tranHandFee) {
        this.tranHandFee = tranHandFee;
    }

    /**
     * 获取当天交易总笔数
     *
     * @return tran_count - 当天交易总笔数
     */
    public BigDecimal getTranCount() {
        return tranCount;
    }

    /**
     * 设置当天交易总笔数
     *
     * @param tranCount 当天交易总笔数
     */
    public void setTranCount(BigDecimal tranCount) {
        this.tranCount = tranCount;
    }

    /**
     * 获取交易网端会员最新的可用余额
     *
     * @return new_balance - 交易网端会员最新的可用余额
     */
    public BigDecimal getNewBalance() {
        return newBalance;
    }

    /**
     * 设置交易网端会员最新的可用余额
     *
     * @param newBalance 交易网端会员最新的可用余额
     */
    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    /**
     * 获取交易网端会员最新的冻结余额
     *
     * @return new_freeze_amount - 交易网端会员最新的冻结余额
     */
    public BigDecimal getNewFreezeAmount() {
        return newFreezeAmount;
    }

    /**
     * 设置交易网端会员最新的冻结余额
     *
     * @param newFreezeAmount 交易网端会员最新的冻结余额
     */
    public void setNewFreezeAmount(BigDecimal newFreezeAmount) {
        this.newFreezeAmount = newFreezeAmount;
    }

    /**
     * 获取说明
     *
     * @return note - 说明
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置说明
     *
     * @param note 说明
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * 获取保留域
     *
     * @return reserve - 保留域
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * 设置保留域
     *
     * @param reserve 保留域
     */
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    /**
     * 获取失败错误码
     *
     * @return error_code - 失败错误码
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * 设置失败错误码
     *
     * @param errorCode 失败错误码
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * 获取失败原因
     *
     * @return fail_reason - 失败原因
     */
    public String getFailReason() {
        return failReason;
    }

    /**
     * 设置失败原因
     *
     * @param failReason 失败原因
     */
    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取是否删除(1:正常，0:删除)
     *
     * @return flag - 是否删除(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(1:正常，0:删除)
     *
     * @param flag 是否删除(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}