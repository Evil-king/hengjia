package com.baibei.hengjia.api.modules.shop.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_shp_logistics")
public class ShpLogistics {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 物流单号
     */
    @Column(name = "logistics_no")
    private String logisticsNo;

    /**
     * 物流类型
     */
    @Column(name = "logistice_type")
    private String logisticeType;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 收货时间
     */
    @Column(name = "received_time")
    private Date receivedTime;

    /**
     * 发货时间
     */
    @Column(name = "pending_time")
    private Date pendingTime;

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
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 获取物流单号
     *
     * @return logistics_no - 物流单号
     */
    public String getLogisticsNo() {
        return logisticsNo;
    }

    /**
     * 设置物流单号
     *
     * @param logisticsNo 物流单号
     */
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    /**
     * 获取物流类型
     *
     * @return logistice_type - 物流类型
     */
    public String getLogisticeType() {
        return logisticeType;
    }

    /**
     * 设置物流类型
     *
     * @param logisticeType 物流类型
     */
    public void setLogisticeType(String logisticeType) {
        this.logisticeType = logisticeType;
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
     * 获取收货时间
     *
     * @return received_time - 收货时间
     */
    public Date getReceivedTime() {
        return receivedTime;
    }

    /**
     * 设置收货时间
     *
     * @param receivedTime 收货时间
     */
    public void setReceivedTime(Date receivedTime) {
        this.receivedTime = receivedTime;
    }

    /**
     * 获取发货时间
     *
     * @return pending_time - 发货时间
     */
    public Date getPendingTime() {
        return pendingTime;
    }

    /**
     * 设置发货时间
     *
     * @param pendingTime 发货时间
     */
    public void setPendingTime(Date pendingTime) {
        this.pendingTime = pendingTime;
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