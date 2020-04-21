package com.baibei.hengjia.admin.modules.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_bank_order")
public class BankOrder {
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
    @Column(name = "serial_no")
    private Integer serialNo;

    /**
     * 记账标志(1：出金 2：入金 3：挂账)
     */
    private Byte type;

    /**
     * 处理标志(挂账才有值)
     */
    @Column(name = "deal_flag")
    private String dealFlag;

    /**
     * 付款人账号
     */
    private String payer;

    /**
     * 收款人账号
     */
    private String payee;

    /**
     * 交易网会员代码
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 子账户
     */
    @Column(name = "sub_account")
    private String subAccount;

    /**
     * 子账户名称
     */
    @Column(name = "sub_account_name")
    private String subAccountName;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 支付手续费子账号
     */
    @Column(name = "pay_fee_account")
    private String payFeeAccount;

    /**
     * 支付子账号名称
     */
    @Column(name = "pay_sub_account_name")
    private String paySubAccountName;

    /**
     * 交易日期
     */
    @Column(name = "pay_date")
    private String payDate;

    /**
     * 交易时间
     */
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 银行前置流水号
     */
    @Column(name = "bank_serial_no")
    private String bankSerialNo;

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
     * @return serial_no - 序号
     */
    public Integer getSerialNo() {
        return serialNo;
    }

    /**
     * 设置序号
     *
     * @param serialNo 序号
     */
    public void setSerialNo(Integer serialNo) {
        this.serialNo = serialNo;
    }

    /**
     * 获取记账标志(1：出金 2：入金 3：挂账)
     *
     * @return type - 记账标志(1：出金 2：入金 3：挂账)
     */
    public Byte getType() {
        return type;
    }

    /**
     * 设置记账标志(1：出金 2：入金 3：挂账)
     *
     * @param type 记账标志(1：出金 2：入金 3：挂账)
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取处理标志(挂账才有值)
     *
     * @return deal_flag - 处理标志(挂账才有值)
     */
    public String getDealFlag() {
        return dealFlag;
    }

    /**
     * 设置处理标志(挂账才有值)
     *
     * @param dealFlag 处理标志(挂账才有值)
     */
    public void setDealFlag(String dealFlag) {
        this.dealFlag = dealFlag;
    }

    /**
     * 获取付款人账号
     *
     * @return payer - 付款人账号
     */
    public String getPayer() {
        return payer;
    }

    /**
     * 设置付款人账号
     *
     * @param payer 付款人账号
     */
    public void setPayer(String payer) {
        this.payer = payer;
    }

    /**
     * 获取收款人账号
     *
     * @return payee - 收款人账号
     */
    public String getPayee() {
        return payee;
    }

    /**
     * 设置收款人账号
     *
     * @param payee 收款人账号
     */
    public void setPayee(String payee) {
        this.payee = payee;
    }

    /**
     * 获取交易网会员代码
     *
     * @return member_no - 交易网会员代码
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置交易网会员代码
     *
     * @param memberNo 交易网会员代码
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取子账户
     *
     * @return sub_account - 子账户
     */
    public String getSubAccount() {
        return subAccount;
    }

    /**
     * 设置子账户
     *
     * @param subAccount 子账户
     */
    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    /**
     * 获取子账户名称
     *
     * @return sub_account_name - 子账户名称
     */
    public String getSubAccountName() {
        return subAccountName;
    }

    /**
     * 设置子账户名称
     *
     * @param subAccountName 子账户名称
     */
    public void setSubAccountName(String subAccountName) {
        this.subAccountName = subAccountName;
    }

    /**
     * 获取交易金额
     *
     * @return amount - 交易金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置交易金额
     *
     * @param amount 交易金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取手续费
     *
     * @return fee - 手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置手续费
     *
     * @param fee 手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取支付手续费子账号
     *
     * @return pay_fee_account - 支付手续费子账号
     */
    public String getPayFeeAccount() {
        return payFeeAccount;
    }

    /**
     * 设置支付手续费子账号
     *
     * @param payFeeAccount 支付手续费子账号
     */
    public void setPayFeeAccount(String payFeeAccount) {
        this.payFeeAccount = payFeeAccount;
    }

    /**
     * 获取支付子账号名称
     *
     * @return pay_sub_account_name - 支付子账号名称
     */
    public String getPaySubAccountName() {
        return paySubAccountName;
    }

    /**
     * 设置支付子账号名称
     *
     * @param paySubAccountName 支付子账号名称
     */
    public void setPaySubAccountName(String paySubAccountName) {
        this.paySubAccountName = paySubAccountName;
    }

    /**
     * 获取交易日期
     *
     * @return pay_date - 交易日期
     */
    public String getPayDate() {
        return payDate;
    }

    /**
     * 设置交易日期
     *
     * @param payDate 交易日期
     */
    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    /**
     * 获取交易时间
     *
     * @return pay_time - 交易时间
     */
    public String getPayTime() {
        return payTime;
    }

    /**
     * 设置交易时间
     *
     * @param payTime 交易时间
     */
    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    /**
     * 获取银行前置流水号
     *
     * @return bank_serial_no - 银行前置流水号
     */
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    /**
     * 设置银行前置流水号
     *
     * @param bankSerialNo 银行前置流水号
     */
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
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