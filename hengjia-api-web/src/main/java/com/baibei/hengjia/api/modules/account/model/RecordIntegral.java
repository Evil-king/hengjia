package com.baibei.hengjia.api.modules.account.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_record_integral")
public class RecordIntegral {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 流水号
     */
    @Column(name = "record_no")
    private String recordNo;

    /**
     * 变动积分
     */
    @Column(name = "change_amount")
    private String changeAmount;

    /**
     * 变动后的余额
     */
    private BigDecimal balance;

    /**
     * 交易类型（1：交易获得积分，2：积分仓购买商品等等）
     */
    @Column(name = "trade_type")
    private Byte tradeType;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 积分类型id
     */
    @Column(name = "integral_no")
    private Long integralNo;

    /**
     * 收入或支出（1：收入2：支出）
     */
    private Byte retype;

    /**
     * 备注
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
     * 删除状态（1：有效，0:无效）
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
     * 获取交易商编码
     *
     * @return customer_no - 交易商编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置交易商编码
     *
     * @param customerNo 交易商编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取流水号
     *
     * @return record_no - 流水号
     */
    public String getRecordNo() {
        return recordNo;
    }

    /**
     * 设置流水号
     *
     * @param recordNo 流水号
     */
    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    /**
     * 获取变动积分
     *
     * @return change_amount - 变动积分
     */
    public String getChangeAmount() {
        return changeAmount;
    }

    /**
     * 设置变动积分
     *
     * @param changeAmount 变动积分
     */
    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    /**
     * 获取变动后的余额
     *
     * @return balance - 变动后的余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置变动后的余额
     *
     * @param balance 变动后的余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取交易类型（1：交易获得积分，2：积分仓购买商品等等）
     *
     * @return trade_type - 交易类型（1：交易获得积分，2：积分仓购买商品等等）
     */
    public Byte getTradeType() {
        return tradeType;
    }

    /**
     * 设置交易类型（1：交易获得积分，2：积分仓购买商品等等）
     *
     * @param tradeType 交易类型（1：交易获得积分，2：积分仓购买商品等等）
     */
    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * 获取订单号
     *
     * @return order_no - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取积分类型id
     *
     * @return integral_no - 积分类型id
     */
    public Long getIntegralNo() {
        return integralNo;
    }

    /**
     * 设置积分类型id
     *
     * @param integralNo 积分类型id
     */
    public void setIntegralNo(Long integralNo) {
        this.integralNo = integralNo;
    }

    /**
     * 获取收入或支出（1：收入2：支出）
     *
     * @return retype - 收入或支出（1：收入2：支出）
     */
    public Byte getRetype() {
        return retype;
    }

    /**
     * 设置收入或支出（1：收入2：支出）
     *
     * @param retype 收入或支出（1：收入2：支出）
     */
    public void setRetype(Byte retype) {
        this.retype = retype;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
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
     * 获取删除状态（1：有效，0:无效）
     *
     * @return flag - 删除状态（1：有效，0:无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置删除状态（1：有效，0:无效）
     *
     * @param flag 删除状态（1：有效，0:无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}