package com.baibei.hengjia.api.modules.match.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_cp_exchange_log")
public class ExchangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流水号
     */
    @Column(name = "exchange_no")
    private String exchangeNo;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 挂牌商编码
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 兑换数量
     */
    @Column(name = "exchange_num")
    private Integer exchangeNum;

    /**
     * 零售价
     */
    @Column(name = "retail_price")
    private BigDecimal retailPrice;

    /**
     * 商品总价
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 使用券金额
     */
    @Column(name = "coupon_price")
    private BigDecimal couponPrice;

    /**
     * 实付金额
     */
    @Column(name = "real_price")
    private BigDecimal realPrice;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 兑换时间
     */
    @Column(name = "exchange_time")
    private Date exchangeTime;

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

    public String getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(String exchangeNo) {
        this.exchangeNo = exchangeNo;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取挂牌商编码
     *
     * @return member_no - 挂牌商编码
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置挂牌商编码
     *
     * @param memberNo 挂牌商编码
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取兑换数量
     *
     * @return exchange_num - 兑换数量
     */
    public Integer getExchangeNum() {
        return exchangeNum;
    }

    /**
     * 设置兑换数量
     *
     * @param exchangeNum 兑换数量
     */
    public void setExchangeNum(Integer exchangeNum) {
        this.exchangeNum = exchangeNum;
    }

    /**
     * 获取零售价
     *
     * @return retail_price - 零售价
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    /**
     * 设置零售价
     *
     * @param retailPrice 零售价
     */
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * 获取商品总价
     *
     * @return total_price - 商品总价
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置商品总价
     *
     * @param totalPrice 商品总价
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取使用券金额
     *
     * @return coupon_price - 使用券金额
     */
    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    /**
     * 设置使用券金额
     *
     * @param couponPrice 使用券金额
     */
    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    /**
     * 获取实付金额
     *
     * @return real_price - 实付金额
     */
    public BigDecimal getRealPrice() {
        return realPrice;
    }

    /**
     * 设置实付金额
     *
     * @param realPrice 实付金额
     */
    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    /**
     * 获取手续费
     *
     * @return fee - 手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置手续费
     *
     * @param fee 手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取兑换时间
     *
     * @return exchange_time - 兑换时间
     */
    public Date getExchangeTime() {
        return exchangeTime;
    }

    /**
     * 设置兑换时间
     *
     * @param exchangeTime 兑换时间
     */
    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
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
}