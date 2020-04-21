package com.baibei.hengjia.api.modules.cash.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_temp_deposit")
public class TempDeposit {
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
     * 入金金额
     */
    private BigDecimal amount;

    /**
     * 入金订单号
     */
    @Column(name = "deposit_no")
    private String depositNo;

    /**
     * 外部订单号
     */
    @Column(name = "outorder_no")
    private String outorderNo;

    /**
     * 渠道(001:微信支付)
     */
    private String channel;

    /**
     * 支付状态(init 初始化、success、成功 fail、失败)
     */
    private String status;

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
     * 获取入金金额
     *
     * @return amount - 入金金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置入金金额
     *
     * @param amount 入金金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取入金订单号
     *
     * @return deposit_no - 入金订单号
     */
    public String getDepositNo() {
        return depositNo;
    }

    /**
     * 设置入金订单号
     *
     * @param depositNo 入金订单号
     */
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }

    /**
     * 获取外部订单号
     *
     * @return outorder_no - 外部订单号
     */
    public String getOutorderNo() {
        return outorderNo;
    }

    /**
     * 设置外部订单号
     *
     * @param outorderNo 外部订单号
     */
    public void setOutorderNo(String outorderNo) {
        this.outorderNo = outorderNo;
    }

    /**
     * 获取渠道(001:微信支付)
     *
     * @return channel - 渠道(001:微信支付)
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置渠道(001:微信支付)
     *
     * @param channel 渠道(001:微信支付)
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 获取支付状态(init 初始化、success、成功 fail、失败)
     *
     * @return status - 支付状态(init 初始化、success、成功 fail、失败)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置支付状态(init 初始化、success、成功 fail、失败)
     *
     * @param status 支付状态(init 初始化、success、成功 fail、失败)
     */
    public void setStatus(String status) {
        this.status = status;
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