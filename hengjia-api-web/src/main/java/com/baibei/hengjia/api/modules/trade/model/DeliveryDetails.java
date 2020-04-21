package com.baibei.hengjia.api.modules.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_delivery_details")
public class DeliveryDetails {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 提货订单id
     */
    @Column(name = "delivery_id")
    private Long deliveryId;

    /**
     * 持仓id
     */
    @Column(name = "hold_id")
    private Long holdId;

    /**
     * 提货数量（针对某个持仓单）
     */
    @Column(name = "delivery_count")
    private Integer deliveryCount;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除（1：正常；0：已删除）
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
     * 获取提货订单id
     *
     * @return delivery_id - 提货订单id
     */
    public Long getDeliveryId() {
        return deliveryId;
    }

    /**
     * 设置提货订单id
     *
     * @param deliveryId 提货订单id
     */
    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    /**
     * 获取持仓id
     *
     * @return hold_id - 持仓id
     */
    public Long getHoldId() {
        return holdId;
    }

    /**
     * 设置持仓id
     *
     * @param holdId 持仓id
     */
    public void setHoldId(Long holdId) {
        this.holdId = holdId;
    }

    /**
     * 获取提货数量（针对某个持仓单）
     *
     * @return delivery_count - 提货数量（针对某个持仓单）
     */
    public Integer getDeliveryCount() {
        return deliveryCount;
    }

    /**
     * 设置提货数量（针对某个持仓单）
     *
     * @param deliveryCount 提货数量（针对某个持仓单）
     */
    public void setDeliveryCount(Integer deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取是否删除（1：正常；0：已删除）
     *
     * @return flag - 是否删除（1：正常；0：已删除）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除（1：正常；0：已删除）
     *
     * @param flag 是否删除（1：正常；0：已删除）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}