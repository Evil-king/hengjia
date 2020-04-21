package com.baibei.hengjia.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_withdraw_deposit_diff")
public class WithDrawDepositDiff {
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
     * 恒价系统订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 银行系统订单号
     */
    @Column(name = "external_no")
    private String externalNo;

    /**
     * 恒价系统订单状态
     */
    @Column(name = "hengjia_status")
    private String hengjiaStatus;

    /**
     * 银行订单状态
     */
    @Column(name = "bank_status")
    private String bankStatus;

    /**
     * 恒价系统订单金额
     */
    @Column(name = "hengjia_amount")
    private BigDecimal hengjiaAmount;

    /**
     * 银行系统订单金额
     */
    @Column(name = "bank_amount")
    private BigDecimal bankAmount;

    /**
     * 出入金订单类型，withdraw=出金，deposit=入金
     */
    private String type;

    /**
     * 对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致
     */
    @Column(name = "diff_type")
    private String diffType;

    /**
     * 处理状态，wait=待处理，deal=已处理
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
     * 获取恒价系统订单号
     *
     * @return order_no - 恒价系统订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置恒价系统订单号
     *
     * @param orderNo 恒价系统订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取银行系统订单号
     *
     * @return external_no - 银行系统订单号
     */
    public String getExternalNo() {
        return externalNo;
    }

    /**
     * 设置银行系统订单号
     *
     * @param externalNo 银行系统订单号
     */
    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo;
    }

    /**
     * 获取恒价系统订单状态
     *
     * @return hengjia_status - 恒价系统订单状态
     */
    public String getHengjiaStatus() {
        return hengjiaStatus;
    }

    /**
     * 设置恒价系统订单状态
     *
     * @param hengjiaStatus 恒价系统订单状态
     */
    public void setHengjiaStatus(String hengjiaStatus) {
        this.hengjiaStatus = hengjiaStatus;
    }

    /**
     * 获取银行订单状态
     *
     * @return bank_status - 银行订单状态
     */
    public String getBankStatus() {
        return bankStatus;
    }

    /**
     * 设置银行订单状态
     *
     * @param bankStatus 银行订单状态
     */
    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    /**
     * 获取恒价系统订单金额
     *
     * @return hengjia_amount - 恒价系统订单金额
     */
    public BigDecimal getHengjiaAmount() {
        return hengjiaAmount;
    }

    /**
     * 设置恒价系统订单金额
     *
     * @param hengjiaAmount 恒价系统订单金额
     */
    public void setHengjiaAmount(BigDecimal hengjiaAmount) {
        this.hengjiaAmount = hengjiaAmount;
    }

    /**
     * 获取银行系统订单金额
     *
     * @return bank_amount - 银行系统订单金额
     */
    public BigDecimal getBankAmount() {
        return bankAmount;
    }

    /**
     * 设置银行系统订单金额
     *
     * @param bankAmount 银行系统订单金额
     */
    public void setBankAmount(BigDecimal bankAmount) {
        this.bankAmount = bankAmount;
    }

    /**
     * 获取出入金订单类型，withdraw=出金，deposit=入金
     *
     * @return type - 出入金订单类型，withdraw=出金，deposit=入金
     */
    public String getType() {
        return type;
    }

    /**
     * 设置出入金订单类型，withdraw=出金，deposit=入金
     *
     * @param type 出入金订单类型，withdraw=出金，deposit=入金
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致
     *
     * @return diff_type - 对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致
     */
    public String getDiffType() {
        return diffType;
    }

    /**
     * 设置对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致
     *
     * @param diffType 对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致
     */
    public void setDiffType(String diffType) {
        this.diffType = diffType;
    }

    /**
     * 获取处理状态，wait=待处理，deal=已处理
     *
     * @return status - 处理状态，wait=待处理，deal=已处理
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置处理状态，wait=待处理，deal=已处理
     *
     * @param status 处理状态，wait=待处理，deal=已处理
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