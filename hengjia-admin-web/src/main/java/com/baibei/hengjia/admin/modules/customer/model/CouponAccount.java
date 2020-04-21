package com.baibei.hengjia.admin.modules.customer.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_cp_coupon_account")
public class CouponAccount {
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
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 券类型（vouchers:代金券）
     */
    @Column(name = "coupon_type")
    private String couponType;

    /**
     * 兑换金额
     */
    private BigDecimal balance;

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
     * 状态（1:有效 0：无效）
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
     * 获取券类型（vouchers:代金券）
     *
     * @return coupon_type - 券类型（vouchers:代金券）
     */
    public String getCouponType() {
        return couponType;
    }

    /**
     * 设置券类型（vouchers:代金券）
     *
     * @param couponType 券类型（vouchers:代金券）
     */
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    /**
     * 获取兑换金额
     *
     * @return balance - 兑换金额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置兑换金额
     *
     * @param balance 兑换金额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
     * 获取状态（1:有效 0：无效）
     *
     * @return flag - 状态（1:有效 0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1:有效 0：无效）
     *
     * @param flag 状态（1:有效 0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}