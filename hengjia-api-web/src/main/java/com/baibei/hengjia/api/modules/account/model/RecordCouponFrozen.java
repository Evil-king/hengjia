package com.baibei.hengjia.api.modules.account.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_record_coupon_frozen")
public class RecordCouponFrozen {
    /**
     * 主键ID
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
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 流水号
     */
    @Column(name = "record_no")
    private String recordNo;

    /**
     * 券类型（vouchers:代金券,deliveryTicket:提货券）
     */
    @Column(name = "coupon_type")
    private String couponType;

    /**
     * 交易类型（109：冻结收入，110：解冻）
     */
    @Column(name = "trade_type")
    private Byte tradeType;

    /**
     * 变动后冻结金额余额
     */
    @Column(name = "frozen_balance")
    private BigDecimal frozenBalance;

    /**
     * 变动数额
     */
    @Column(name = "change_amount")
    private String changeAmount;

    /**
     * 收入或支出（1：（解冻）支出2：（冻结）收入）
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
     * 状态（1：有效0：无效）
     */
    private Byte flag;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
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
     * 获取商品交易编码
     *
     * @return product_trade_no - 商品交易编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置商品交易编码
     *
     * @param productTradeNo 商品交易编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
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
     * 获取券类型（vouchers:代金券,deliveryTicket:提货券）
     *
     * @return coupon_type - 券类型（vouchers:代金券,deliveryTicket:提货券）
     */
    public String getCouponType() {
        return couponType;
    }

    /**
     * 设置券类型（vouchers:代金券,deliveryTicket:提货券）
     *
     * @param couponType 券类型（vouchers:代金券,deliveryTicket:提货券）
     */
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    /**
     * 获取交易类型（109：冻结收入，110：解冻）
     *
     * @return trade_type - 交易类型（109：冻结收入，110：解冻）
     */
    public Byte getTradeType() {
        return tradeType;
    }

    /**
     * 设置交易类型（109：冻结收入，110：解冻）
     *
     * @param tradeType 交易类型（109：冻结收入，110：解冻）
     */
    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * 获取变动后冻结金额余额
     *
     * @return frozen_balance - 变动后冻结金额余额
     */
    public BigDecimal getFrozenBalance() {
        return frozenBalance;
    }

    /**
     * 设置变动后冻结金额余额
     *
     * @param frozenBalance 变动后冻结金额余额
     */
    public void setFrozenBalance(BigDecimal frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    /**
     * 获取变动数额
     *
     * @return change_amount - 变动数额
     */
    public String getChangeAmount() {
        return changeAmount;
    }

    /**
     * 设置变动数额
     *
     * @param changeAmount 变动数额
     */
    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    /**
     * 获取收入或支出（1：（解冻）支出2：（冻结）收入）
     *
     * @return retype - 收入或支出（1：（解冻）支出2：（冻结）收入）
     */
    public Byte getRetype() {
        return retype;
    }

    /**
     * 设置收入或支出（1：（解冻）支出2：（冻结）收入）
     *
     * @param retype 收入或支出（1：（解冻）支出2：（冻结）收入）
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
     * 获取状态（1：有效0：无效）
     *
     * @return flag - 状态（1：有效0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：有效0：无效）
     *
     * @param flag 状态（1：有效0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}