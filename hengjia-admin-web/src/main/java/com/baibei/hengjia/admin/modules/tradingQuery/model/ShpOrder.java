package com.baibei.hengjia.admin.modules.tradingQuery.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_shp_order")
public class ShpOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 冗余地址表id
     */
    @Column(name = "address_id")
    private Integer addressId;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 订单状态(init:初始化,success:兑换成功(即待发货状态),fail:兑换失败,delivered:已发货(待收货),received:已收货)
     */
    @Column(name = "order_status")
    private String orderStatus;

    /**
     * 支付渠道
     */
    @Column(name = "pay_channel")
    private String payChannel;

    /**
     * 合计积分
     */
    @Column(name = "sum_point")
    private Long sumPoint;

    /**
     * 外部订单号
     */
    @Column(name = "out_ordernum")
    private String outOrdernum;

    /**
     * 物流单号
     */
    @Column(name = "logistics_no")
    private String logisticsNo;

    /**
     * 物流公司名称
     */
    @Column(name = "logistics_company")
    private String logisticsCompany;

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
     * 发货时间
     */
    @Column(name = "delivered_time")
    private Date deliveredTime;

    /**
     * 收货时间
     */
    @Column(name = "received_time")
    private Date receivedTime;

    /**
     * 备注
     */
    private String remark;

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
     * 获取冗余地址表id
     *
     * @return address_id - 冗余地址表id
     */
    public Integer getAddressId() {
        return addressId;
    }

    /**
     * 设置冗余地址表id
     *
     * @param addressId 冗余地址表id
     */
    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
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
     * 获取订单状态(init:初始化,success:兑换成功(即待发货状态),fail:兑换失败,delivered:已发货(待收货),received:已收货)
     *
     * @return order_status - 订单状态(init:初始化,success:兑换成功(即待发货状态),fail:兑换失败,delivered:已发货(待收货),received:已收货)
     */
    public String getOrderStatus() {
        return orderStatus;
    }

    /**
     * 设置订单状态(init:初始化,success:兑换成功(即待发货状态),fail:兑换失败,delivered:已发货(待收货),received:已收货)
     *
     * @param orderStatus 订单状态(init:初始化,success:兑换成功(即待发货状态),fail:兑换失败,delivered:已发货(待收货),received:已收货)
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 获取支付渠道
     *
     * @return pay_channel - 支付渠道
     */
    public String getPayChannel() {
        return payChannel;
    }

    /**
     * 设置支付渠道
     *
     * @param payChannel 支付渠道
     */
    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    /**
     * 获取合计积分
     *
     * @return sum_point - 合计积分
     */
    public Long getSumPoint() {
        return sumPoint;
    }

    /**
     * 设置合计积分
     *
     * @param sumPoint 合计积分
     */
    public void setSumPoint(Long sumPoint) {
        this.sumPoint = sumPoint;
    }

    /**
     * 获取外部订单号
     *
     * @return out_ordernum - 外部订单号
     */
    public String getOutOrdernum() {
        return outOrdernum;
    }

    /**
     * 设置外部订单号
     *
     * @param outOrdernum 外部订单号
     */
    public void setOutOrdernum(String outOrdernum) {
        this.outOrdernum = outOrdernum;
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
     * 获取物流公司名称
     *
     * @return logistics_company - 物流公司名称
     */
    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    /**
     * 设置物流公司名称
     *
     * @param logisticsCompany 物流公司名称
     */
    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
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

    /**
     * 获取发货时间
     *
     * @return delivered_time - 发货时间
     */
    public Date getDeliveredTime() {
        return deliveredTime;
    }

    /**
     * 设置发货时间
     *
     * @param deliveredTime 发货时间
     */
    public void setDeliveredTime(Date deliveredTime) {
        this.deliveredTime = deliveredTime;
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
}