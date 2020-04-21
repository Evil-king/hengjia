package com.baibei.hengjia.api.modules.cash.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_temp_withdraw_info")
public class WithdrawInfo {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 银行账户
     */
    @Column(name = "bank_account")
    private String bankAccount;

    /**
     * 开户银行
     */
    @Column(name = "account_bank")
    private String accountBank;

    /**
     * 开户支行
     */
    @Column(name = "branch_bank")
    private String branchBank;

    /**
     * 银行卡号
     */
    @Column(name = "bank_num")
    private String bankNum;

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
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户编号
     *
     * @return customer_no - 用户编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置用户编号
     *
     * @param customerNo 用户编号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取银行账户
     *
     * @return bank_account - 银行账户
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * 设置银行账户
     *
     * @param bankAccount 银行账户
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * 获取开户银行
     *
     * @return account_bank - 开户银行
     */
    public String getAccountBank() {
        return accountBank;
    }

    /**
     * 设置开户银行
     *
     * @param accountBank 开户银行
     */
    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    /**
     * 获取开户支行
     *
     * @return branch_bank - 开户支行
     */
    public String getBranchBank() {
        return branchBank;
    }

    /**
     * 设置开户支行
     *
     * @param branchBank 开户支行
     */
    public void setBranchBank(String branchBank) {
        this.branchBank = branchBank;
    }

    /**
     * 获取银行卡号
     *
     * @return bank_num - 银行卡号
     */
    public String getBankNum() {
        return bankNum;
    }

    /**
     * 设置银行卡号
     *
     * @param bankNum 银行卡号
     */
    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
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
}