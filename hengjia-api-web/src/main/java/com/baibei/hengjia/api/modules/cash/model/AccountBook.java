package com.baibei.hengjia.api.modules.cash.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_tra_account_book")
public class AccountBook {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 子账户
     */
    @Column(name = "cust_acctId")
    private String custAcctid;

    /**
     * 虚拟账号
     */
    @Column(name = "cust_flag")
    private Byte custFlag;

    /**
     * 1-普通会员子账户 2-挂账子账户  3-手续费子账户 4-利息子账户 6-清收子账户
     */
    @Column(name = "cust_type")
    private Byte custType;

    /**
     * 子账户状态 1：正常  2：已销户
     */
    @Column(name = "cust_status")
    private Byte custStatus;

    /**
     * 会员编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 上级监管账号
     */
    @Column(name = "main_acctId")
    private String mainAcctid;

    /**
     * 会员名称
     */
    @Column(name = "cust_name")
    private String custName;

    /**
     * 账户总余额
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 账户可用余额
     */
    @Column(name = "total_balance")
    private BigDecimal totalBalance;

    /**
     * 账户总冻结金额
     */
    @Column(name = "total_freeze_amount")
    private BigDecimal totalFreezeAmount;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Byte flag;

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
     * 维护日期
     */
    @Column(name = "tran_date")
    private String tranDate;

    /**
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取子账户
     *
     * @return cust_acctId - 子账户
     */
    public String getCustAcctid() {
        return custAcctid;
    }

    /**
     * 设置子账户
     *
     * @param custAcctid 子账户
     */
    public void setCustAcctid(String custAcctid) {
        this.custAcctid = custAcctid;
    }

    /**
     * 获取虚拟账号
     *
     * @return cust_flag - 虚拟账号
     */
    public Byte getCustFlag() {
        return custFlag;
    }

    /**
     * 设置虚拟账号
     *
     * @param custFlag 虚拟账号
     */
    public void setCustFlag(Byte custFlag) {
        this.custFlag = custFlag;
    }

    /**
     * 获取1-普通会员子账户 2-挂账子账户  3-手续费子账户 4-利息子账户 6-清收子账户
     *
     * @return cust_type - 1-普通会员子账户 2-挂账子账户  3-手续费子账户 4-利息子账户 6-清收子账户
     */
    public Byte getCustType() {
        return custType;
    }

    /**
     * 设置1-普通会员子账户 2-挂账子账户  3-手续费子账户 4-利息子账户 6-清收子账户
     *
     * @param custType 1-普通会员子账户 2-挂账子账户  3-手续费子账户 4-利息子账户 6-清收子账户
     */
    public void setCustType(Byte custType) {
        this.custType = custType;
    }

    /**
     * 获取子账户状态 1：正常  2：已销户
     *
     * @return cust_status - 子账户状态 1：正常  2：已销户
     */
    public Byte getCustStatus() {
        return custStatus;
    }

    /**
     * 设置子账户状态 1：正常  2：已销户
     *
     * @param custStatus 子账户状态 1：正常  2：已销户
     */
    public void setCustStatus(Byte custStatus) {
        this.custStatus = custStatus;
    }

    /**
     * 获取会员编号
     *
     * @return customer_no - 会员编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置会员编号
     *
     * @param customerNo 会员编号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取上级监管账号
     *
     * @return main_acctId - 上级监管账号
     */
    public String getMainAcctid() {
        return mainAcctid;
    }

    /**
     * 设置上级监管账号
     *
     * @param mainAcctid 上级监管账号
     */
    public void setMainAcctid(String mainAcctid) {
        this.mainAcctid = mainAcctid;
    }

    /**
     * 获取会员名称
     *
     * @return cust_name - 会员名称
     */
    public String getCustName() {
        return custName;
    }

    /**
     * 设置会员名称
     *
     * @param custName 会员名称
     */
    public void setCustName(String custName) {
        this.custName = custName;
    }

    /**
     * 获取账户总余额
     *
     * @return total_amount - 账户总余额
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置账户总余额
     *
     * @param totalAmount 账户总余额
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * 获取账户可用余额
     *
     * @return total_balance - 账户可用余额
     */
    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    /**
     * 设置账户可用余额
     *
     * @param totalBalance 账户可用余额
     */
    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    /**
     * 获取账户总冻结金额
     *
     * @return total_freeze_amount - 账户总冻结金额
     */
    public BigDecimal getTotalFreezeAmount() {
        return totalFreezeAmount;
    }

    /**
     * 设置账户总冻结金额
     *
     * @param totalFreezeAmount 账户总冻结金额
     */
    public void setTotalFreezeAmount(BigDecimal totalFreezeAmount) {
        this.totalFreezeAmount = totalFreezeAmount;
    }

    /**
     * 获取状态(1:正常，0:禁用)
     *
     * @return flag - 状态(1:正常，0:禁用)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:禁用)
     *
     * @param flag 状态(1:正常，0:禁用)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
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
     * 获取维护日期
     *
     * @return tran_date - 维护日期
     */
    public String getTranDate() {
        return tranDate;
    }

    /**
     * 设置维护日期
     *
     * @param tranDate 维护日期
     */
    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }
}