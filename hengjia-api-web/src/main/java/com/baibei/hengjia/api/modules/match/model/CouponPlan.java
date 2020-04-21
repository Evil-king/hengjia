package com.baibei.hengjia.api.modules.match.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_cp_coupon_plan")
public class CouponPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 计划名称
     */
    @Column(name = "plan_title")
    private String planTitle;

    /**
     * 合计金额
     */
    @Column(name = "total_price")
    private BigDecimal totalPrice;

    /**
     * 状态（wait:待执行；deal:已执行）
     */
    private String status;

    /**
     * 执行时间
     */
    @Column(name = "deal_time")
    private Date dealTime;

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
     * 获取计划名称
     *
     * @return plan_title - 计划名称
     */
    public String getPlanTitle() {
        return planTitle;
    }

    /**
     * 设置计划名称
     *
     * @param planTitle 计划名称
     */
    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    /**
     * 获取合计金额
     *
     * @return total_price - 合计金额
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * 设置合计金额
     *
     * @param totalPrice 合计金额
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * 获取状态（wait:待执行；deal:已执行）
     *
     * @return status - 状态（wait:待执行；deal:已执行）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（wait:待执行；deal:已执行）
     *
     * @param status 状态（wait:待执行；deal:已执行）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取执行时间
     *
     * @return deal_time - 执行时间
     */
    public Date getDealTime() {
        return dealTime;
    }

    /**
     * 设置执行时间
     *
     * @param dealTime 执行时间
     */
    public void setDealTime(Date dealTime) {
        this.dealTime = dealTime;
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