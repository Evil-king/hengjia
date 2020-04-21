package com.baibei.hengjia.api.modules.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_cust_dz_fail")
public class CustDzFail {
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
     * 银行清算后可用余额
     */
    @Column(name = "bank_balance")
    private BigDecimal bankBalance;

    /**
     * 银行清算后冻结余额
     */
    @Column(name = "bank_frozen_amount")
    private BigDecimal bankFrozenAmount;

    /**
     * 交易网可用余额
     */
    @Column(name = "hengjia_balance")
    private BigDecimal hengjiaBalance;

    /**
     * 交易网冻结余额
     */
    @Column(name = "hengjia_frozen_amount")
    private BigDecimal hengjiaFrozenAmount;

    /**
     * 可用余额差额（银行可用余额-交易网可用余额）
     */
    @Column(name = "balance_diff")
    private BigDecimal balanceDiff;

    /**
     * 冻结余额差额（银行冻结余额-交易网冻结余额）
     */
    @Column(name = "frozen_diff")
    private BigDecimal frozenDiff;

    /**
     * 对账结果说明
     */
    private String remark;

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
     * 获取银行清算后可用余额
     *
     * @return bank_balance - 银行清算后可用余额
     */
    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    /**
     * 设置银行清算后可用余额
     *
     * @param bankBalance 银行清算后可用余额
     */
    public void setBankBalance(BigDecimal bankBalance) {
        this.bankBalance = bankBalance;
    }

    /**
     * 获取银行清算后冻结余额
     *
     * @return bank_frozen_amount - 银行清算后冻结余额
     */
    public BigDecimal getBankFrozenAmount() {
        return bankFrozenAmount;
    }

    /**
     * 设置银行清算后冻结余额
     *
     * @param bankFrozenAmount 银行清算后冻结余额
     */
    public void setBankFrozenAmount(BigDecimal bankFrozenAmount) {
        this.bankFrozenAmount = bankFrozenAmount;
    }

    /**
     * 获取交易网可用余额
     *
     * @return hengjia_balance - 交易网可用余额
     */
    public BigDecimal getHengjiaBalance() {
        return hengjiaBalance;
    }

    /**
     * 设置交易网可用余额
     *
     * @param hengjiaBalance 交易网可用余额
     */
    public void setHengjiaBalance(BigDecimal hengjiaBalance) {
        this.hengjiaBalance = hengjiaBalance;
    }

    /**
     * 获取交易网冻结余额
     *
     * @return hengjia_frozen_amount - 交易网冻结余额
     */
    public BigDecimal getHengjiaFrozenAmount() {
        return hengjiaFrozenAmount;
    }

    /**
     * 设置交易网冻结余额
     *
     * @param hengjiaFrozenAmount 交易网冻结余额
     */
    public void setHengjiaFrozenAmount(BigDecimal hengjiaFrozenAmount) {
        this.hengjiaFrozenAmount = hengjiaFrozenAmount;
    }

    /**
     * 获取可用余额差额（银行可用余额-交易网可用余额）
     *
     * @return balance_diff - 可用余额差额（银行可用余额-交易网可用余额）
     */
    public BigDecimal getBalanceDiff() {
        return balanceDiff;
    }

    /**
     * 设置可用余额差额（银行可用余额-交易网可用余额）
     *
     * @param balanceDiff 可用余额差额（银行可用余额-交易网可用余额）
     */
    public void setBalanceDiff(BigDecimal balanceDiff) {
        this.balanceDiff = balanceDiff;
    }

    /**
     * 获取冻结余额差额（银行冻结余额-交易网冻结余额）
     *
     * @return frozen_diff - 冻结余额差额（银行冻结余额-交易网冻结余额）
     */
    public BigDecimal getFrozenDiff() {
        return frozenDiff;
    }

    /**
     * 设置冻结余额差额（银行冻结余额-交易网冻结余额）
     *
     * @param frozenDiff 冻结余额差额（银行冻结余额-交易网冻结余额）
     */
    public void setFrozenDiff(BigDecimal frozenDiff) {
        this.frozenDiff = frozenDiff;
    }

    /**
     * 获取对账结果说明
     *
     * @return remark - 对账结果说明
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置对账结果说明
     *
     * @param remark 对账结果说明
     */
    public void setRemark(String remark) {
        this.remark = remark;
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