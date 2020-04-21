package com.baibei.hengjia.api.modules.cash.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_order_deposit")
public class OrderDeposit {
    /**
     * 自增主键
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
     * 入金账号
     */
    private String account;

    /**
     * 入金订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 入金金额
     */
    @Column(name = "order_amt")
    private BigDecimal orderAmt;

    /**
     * 交易类型
     */
    @Column(name = "order_type")
    private String orderType;

    /**
     * 账号名称
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 保留域1
     */
    private String reserved01;

    /**
     * 保留域2
     */
    private String reserved02;

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
     * 是否删除(0:删除,1:未删除)
     */
    private Byte flag;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 银行记账日期
     */
    @Column(name = "acct_date")
    private Date acctDate;

    /**
     * 币种
     */
    @Column(name = "ccy_code")
    private String ccyCode;

    /**
     * 外部订单号
     */
    @Column(name = "external_no")
    private String externalNo;

    /**
     * 资金汇总账号
     */
    @Column(name = "sup_acct_Id")
    private String supAcctId;

    /**
     * 入金订单状态(success,fail,wait)
     */
    private String status;

    @Column(name = "cust_acct_Id")
    private String custAcctId;

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
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
     * 获取入金账号
     *
     * @return account - 入金账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置入金账号
     *
     * @param account 入金账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取入金订单号
     *
     * @return order_no - 入金订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置入金订单号
     *
     * @param orderNo 入金订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取入金金额
     *
     * @return order_amt - 入金金额
     */
    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    /**
     * 设置入金金额
     *
     * @param orderAmt 入金金额
     */
    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    /**
     * 获取交易类型
     *
     * @return order_type - 交易类型
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * 设置交易类型
     *
     * @param orderType 交易类型
     */
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    /**
     * 获取账号名称
     *
     * @return account_name - 账号名称
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置账号名称
     *
     * @param accountName 账号名称
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取保留域1
     *
     * @return reserved01 - 保留域1
     */
    public String getReserved01() {
        return reserved01;
    }

    /**
     * 设置保留域1
     *
     * @param reserved01 保留域1
     */
    public void setReserved01(String reserved01) {
        this.reserved01 = reserved01;
    }

    /**
     * 获取保留域2
     *
     * @return reserved02 - 保留域2
     */
    public String getReserved02() {
        return reserved02;
    }

    /**
     * 设置保留域2
     *
     * @param reserved02 保留域2
     */
    public void setReserved02(String reserved02) {
        this.reserved02 = reserved02;
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

    /**
     * 获取银行记账日期
     *
     * @return acct_date - 银行记账日期
     */
    public Date getAcctDate() {
        return acctDate;
    }

    /**
     * 设置银行记账日期
     *
     * @param acctDate 银行记账日期
     */
    public void setAcctDate(Date acctDate) {
        this.acctDate = acctDate;
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
     * 获取资金汇总账号
     *
     * @return sup_acct_Id - 资金汇总账号
     */
    public String getSupAcctId() {
        return supAcctId;
    }

    /**
     * 设置资金汇总账号
     *
     * @param supAcctId 资金汇总账号
     */
    public void setSupAcctId(String supAcctId) {
        this.supAcctId = supAcctId;
    }

    /**
     * 获取入金订单状态(success,fail,wait)
     *
     * @return status - 入金订单状态(success,fail,wait)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置入金订单状态(success,fail,wait)
     *
     * @param status 入金订单状态(success,fail,wait)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 会员子账号
     * @return
     */
    public String getCustAcctId() {
        return custAcctId;
    }

    public void setCustAcctId(String custAcctId) {
        this.custAcctId = custAcctId;
    }
}