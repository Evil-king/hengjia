package com.baibei.hengjia.api.modules.match.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_cp_coupon_offset")
public class CouponOffset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流水号
     */
    @Column(name = "offset_no")
    private String offsetNo;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 应扣减的券余额总数
     */
    @Column(name = "detuch_coupon_acct")
    private BigDecimal detuchCouponAcct;

    /**
     * 需补总金额（钱）
     */
    @Column(name = "offset_amount")
    private BigDecimal offsetAmount;

    /**
     * 抵扣数量
     */
    @Column(name = "offset_count")
    private Integer offsetCount;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 买方手续费
     */
    @Column(name = "buy_fee")
    private BigDecimal buyFee;

    /**
     * 卖方手续费
     */
    @Column(name = "sell_fee")
    private BigDecimal sellFee;

    /**
     * 解锁时间
     */
    @Column(name = "trade_day")
    private Date tradeDay;
    /**
     * 状态。success=成功；fail=失败
     */
    @Column(name = "status")
    private String status;

    /**
     * 失败类型（balanceLimit=余额不足，couponBalanceLimit=券余额不足）
     */
    @Column(name = "fail_type")
    private String failType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    private Byte flag;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取流水号
     *
     * @return offset_no - 流水号
     */
    public String getOffsetNo() {
        return offsetNo;
    }

    /**
     * 设置流水号
     *
     * @param offsetNo 流水号
     */
    public void setOffsetNo(String offsetNo) {
        this.offsetNo = offsetNo;
    }

    /**
     * @return customer_no
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * @param customerNo
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * @return product_trade_no
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * @param productTradeNo
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取应扣减的券余额总数
     *
     * @return detuch_coupon_acct - 应扣减的券余额总数
     */
    public BigDecimal getDetuchCouponAcct() {
        return detuchCouponAcct;
    }

    /**
     * 设置应扣减的券余额总数
     *
     * @param detuchCouponAcct 应扣减的券余额总数
     */
    public void setDetuchCouponAcct(BigDecimal detuchCouponAcct) {
        this.detuchCouponAcct = detuchCouponAcct;
    }

    /**
     * 获取需补总金额（钱）
     *
     * @return offset_amount - 需补总金额（钱）
     */
    public BigDecimal getOffsetAmount() {
        return offsetAmount;
    }

    /**
     * 设置需补总金额（钱）
     *
     * @param offsetAmount 需补总金额（钱）
     */
    public void setOffsetAmount(BigDecimal offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    /**
     * 获取抵扣数量
     *
     * @return offset_count - 抵扣数量
     */
    public Integer getOffsetCount() {
        return offsetCount;
    }

    /**
     * 设置抵扣数量
     *
     * @param offsetCount 抵扣数量
     */
    public void setOffsetCount(Integer offsetCount) {
        this.offsetCount = offsetCount;
    }

    /**
     * 获取成本
     *
     * @return cost - 成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本
     *
     * @param cost 成本
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取买方手续费
     *
     * @return buy_fee - 买方手续费
     */
    public BigDecimal getBuyFee() {
        return buyFee;
    }

    /**
     * 设置买方手续费
     *
     * @param buyFee 买方手续费
     */
    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }

    /**
     * 获取卖方手续费
     *
     * @return sell_fee - 卖方手续费
     */
    public BigDecimal getSellFee() {
        return sellFee;
    }

    /**
     * 设置卖方手续费
     *
     * @param sellFee 卖方手续费
     */
    public void setSellFee(BigDecimal sellFee) {
        this.sellFee = sellFee;
    }

    /**
     * 获取解锁时间
     *
     * @return trade_day - 解锁时间
     */
    public Date getTradeDay() {
        return tradeDay;
    }

    /**
     * 设置解锁时间
     *
     * @param tradeDay 解锁时间
     */
    public void setTradeDay(Date tradeDay) {
        this.tradeDay = tradeDay;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return flag
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * @param flag
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailType() {
        return failType;
    }

    public void setFailType(String failType) {
        this.failType = failType;
    }
}