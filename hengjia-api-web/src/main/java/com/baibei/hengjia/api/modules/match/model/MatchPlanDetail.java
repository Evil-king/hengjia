package com.baibei.hengjia.api.modules.match.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_cp_match_plan_detail")
public class MatchPlanDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联计划Id
     */
    @Column(name = "plan_id")
    private Long planId;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "product_trade_no")
    private String productTradeNo;

    @Column(name = "thaw_amount")
    private BigDecimal thawAmount;

    @Column(name = "frozen_amount")
    private BigDecimal frozenAmount;

    public BigDecimal getThawAmount() {
        return thawAmount;
    }

    public void setThawAmount(BigDecimal thawAmount) {
        this.thawAmount = thawAmount;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 券类型
     */
    private String type;
    /**
     * 流水号
     */
    private String planNo;

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    /**
     * 金额
     */
    private BigDecimal price;

    /**
     * 来源（ASSEMBLE_COUPON:拼团代金券 GROUP_COUPON：组团代金券）
     */
    private String source;

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
     * 获取关联计划Id
     *
     * @return plan_id - 关联计划Id
     */
    public Long getPlanId() {
        return planId;
    }

    /**
     * 设置关联计划Id
     *
     * @param planId 关联计划Id
     */
    public void setPlanId(Long planId) {
        this.planId = planId;
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
     * 获取券类型
     *
     * @return type - 券类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置券类型
     *
     * @param type 券类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取金额
     *
     * @return price - 金额
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置金额
     *
     * @param price 金额
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取来源（ASSEMBLE_COUPON:拼团代金券 GROUP_COUPON：组团代金券）
     *
     * @return source - 来源（ASSEMBLE_COUPON:拼团代金券 GROUP_COUPON：组团代金券）
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置来源（ASSEMBLE_COUPON:拼团代金券 GROUP_COUPON：组团代金券）
     *
     * @param source 来源（ASSEMBLE_COUPON:拼团代金券 GROUP_COUPON：组团代金券）
     */
    public void setSource(String source) {
        this.source = source;
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