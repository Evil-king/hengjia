package com.baibei.hengjia.admin.modules.withdraw.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_tra_order_withdraw")
public class OrderWithdraw {
    /**
     * 自增长主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 出金账号(出金银行账号)
     */
    private String account;

    /**
     * 出金账户名称
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 出金流水号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 出金金额
     */
    @Column(name = "orderAmt")
    private BigDecimal orderamt;

    /**
     * 出金类型(1、交易网发起的 2、银行发起的 3、调账)
     */
    @Column(name = "order_type")
    private String orderType;

    /**
     * 出金银行名称
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 手续费
     */
    @Column(name = "handel_fee")
    private BigDecimal handelFee;

    /**
     * 状态(1、提现申请中 2、提现审核通过(后台操作的) 3、提现处理中 4、提现成功 5、提现失败、6、审核不通过)
     */
    private String status;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 审核时间
     */
    @Column(name = "reviewer_time")
    private Date reviewerTime;

    /**
     * 是否删除(0:删除,1:未删除)
     */
    private Byte flag;

    /**
     * 是否扫描 0:已处理、1:未处理(失败订单)
     */
    private Byte effective;

    /**
     * 外部订单号
     */
    @Column(name = "external_no")
    private String externalNo;

    /**
     * 资金汇总账户
     */
    @Column(name = "sup_acct_Id")
    private String supAcctId;

    /**
     * 会员子账号
     */
    @Column(name = "cust_acct_Id")
    private String custAcctId;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 获取自增长主键
     *
     * @return id - 自增长主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增长主键
     *
     * @param id 自增长主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取客户编号
     *
     * @return customer_no - 客户编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置客户编号
     *
     * @param customerNo 客户编号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取出金账号(出金银行账号)
     *
     * @return account - 出金账号(出金银行账号)
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置出金账号(出金银行账号)
     *
     * @param account 出金账号(出金银行账号)
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取出金账户名称
     *
     * @return account_name - 出金账户名称
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置出金账户名称
     *
     * @param accountName 出金账户名称
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取出金流水号
     *
     * @return order_no - 出金流水号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置出金流水号
     *
     * @param orderNo 出金流水号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取出金金额
     *
     * @return orderAmt - 出金金额
     */
    public BigDecimal getOrderamt() {
        return orderamt;
    }

    /**
     * 设置出金金额
     *
     * @param orderamt 出金金额
     */
    public void setOrderamt(BigDecimal orderamt) {
        this.orderamt = orderamt;
    }

    /**
     * 获取出金类型(1、交易网发起的 2、银行发起的 3、调账)
     *
     * @return order_type - 出金类型(1、交易网发起的 2、银行发起的 3、调账)
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置出金类型(1、交易网发起的 2、银行发起的 3、调账)
     *
     * @param orderType 出金类型(1、交易网发起的 2、银行发起的 3、调账)
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取出金银行名称
     *
     * @return bank_name - 出金银行名称
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置出金银行名称
     *
     * @param bankName 出金银行名称
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取手续费
     *
     * @return handel_fee - 手续费
     */
    public BigDecimal getHandelFee() {
        return handelFee;
    }

    /**
     * 设置手续费
     *
     * @param handelFee 手续费
     */
    public void setHandelFee(BigDecimal handelFee) {
        this.handelFee = handelFee;
    }

    /**
     * 获取状态(1、提现申请中 2、提现审核通过(后台操作的) 3、提现处理中 4、提现成功 5、提现失败、6、审核不通过)
     *
     * @return status - 状态(1、提现申请中 2、提现审核通过(后台操作的) 3、提现处理中 4、提现成功 5、提现失败、6、审核不通过)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态(1、提现申请中 2、提现审核通过(后台操作的) 3、提现处理中 4、提现成功 5、提现失败、6、审核不通过)
     *
     * @param status 状态(1、提现申请中 2、提现审核通过(后台操作的) 3、提现处理中 4、提现成功 5、提现失败、6、审核不通过)
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取审核人
     *
     * @return reviewer - 审核人
     */
    public String getReviewer() {
        return reviewer;
    }

    /**
     * 设置审核人
     *
     * @param reviewer 审核人
     */
    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    /**
     * 获取审核时间
     *
     * @return reviewer_time - 审核时间
     */
    public Date getReviewerTime() {
        return reviewerTime;
    }

    /**
     * 设置审核时间
     *
     * @param reviewerTime 审核时间
     */
    public void setReviewerTime(Date reviewerTime) {
        this.reviewerTime = reviewerTime;
    }

    /**
     * 获取是否删除(0:删除,1:未删除)
     *
     * @return flag - 是否删除(0:删除,1:未删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(0:删除,1:未删除)
     *
     * @param flag 是否删除(0:删除,1:未删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * 获取是否扫描 0:已处理、1:未处理(失败订单)
     *
     * @return effective - 是否扫描 0:已处理、1:未处理(失败订单)
     */
    public Byte getEffective() {
        return effective;
    }

    /**
     * 设置是否扫描 0:已处理、1:未处理(失败订单)
     *
     * @param effective 是否扫描 0:已处理、1:未处理(失败订单)
     */
    public void setEffective(Byte effective) {
        this.effective = effective;
    }

    /**
     * 获取外部订单号
     *
     * @return external_no - 外部订单号
     */
    public String getExternalNo() {
        return externalNo;
    }

    /**
     * 设置外部订单号
     *
     * @param externalNo 外部订单号
     */
    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo;
    }

    /**
     * 获取资金汇总账户
     *
     * @return sup_acct_Id - 资金汇总账户
     */
    public String getSupAcctId() {
        return supAcctId;
    }

    /**
     * 设置资金汇总账户
     *
     * @param supAcctId 资金汇总账户
     */
    public void setSupAcctId(String supAcctId) {
        this.supAcctId = supAcctId;
    }

    /**
     * 获取会员子账号
     *
     * @return cust_acct_Id - 会员子账号
     */
    public String getCustAcctId() {
        return custAcctId;
    }

    /**
     * 设置会员子账号
     *
     * @param custAcctId 会员子账号
     */
    public void setCustAcctId(String custAcctId) {
        this.custAcctId = custAcctId;
    }

    /**
     * 获取备注
     *
     * @return remarks - 备注
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 设置备注
     *
     * @param remarks 备注
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}