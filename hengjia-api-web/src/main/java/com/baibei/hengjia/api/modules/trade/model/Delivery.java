package com.baibei.hengjia.api.modules.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 商品id
     */
    @Column(name = "product_id")
    private Long productId;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 来源plan为配货
     */
    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 商品交易名称
     */
    @Column(name = "product_trade_name")
    private String productTradeName;

    /**
     * 商品发行价
     */
    @Column(name = "issue_price")
    private BigDecimal issuePrice;

    /**
     * 收货地址id
     */
    @Column(name = "address_id")
    private Long addressId;

    /**
     * 提货订单号
     */
    @Column(name = "delivery_no")
    private String deliveryNo;

    /**
     * 运单号
     */
    @Column(name = "logistics_no")
    private String logisticsNo;

    /**
     * 提货订单状态(10:待审核；20:代发货；30:已发货；40:已收货)
     */
    @Column(name = "delivery_status")
    private Integer deliveryStatus;

    /**
     * 申请提货时间
     */
    @Column(name = "delivery_time")
    private Date deliveryTime;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private Date auditTime;

    /**
     * 发货时间
     */
    @Column(name = "pending_time")
    private Date pendingTime;

    /**
     * 收货时间
     */
    @Column(name = "receive_time")
    private Date receiveTime;

    /**
     * 物流类型
     */
    @Column(name = "logistics_type")
    private String logisticsType;

    /**
     * 物流公司名称
     */
    @Column(name = "logistics_company")
    private String logisticsCompany;

    /**
     * 仓储费
     */
    @Column(name = "storage_charge")
    private Long storageCharge;

    /**
     * 出仓费
     */
    @Column(name = "clearance_charge")
    private Long clearanceCharge;

    /**
     * 运费
     */
    private Long freight;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提货数量
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
     * 持仓商品类型（main=本票,match=配票）
     */
    @Column(name = "hold_type")
    private String holdType;

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
     * 获取商品id
     *
     * @return product_id - 商品id
     */
    public Long getProductId() {
        return productId;
    }

    /**
     * 设置商品id
     *
     * @param productId 商品id
     */
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * 获取收货地址id
     *
     * @return address_id - 收货地址id
     */
    public Long getAddressId() {
        return addressId;
    }

    /**
     * 设置收货地址id
     *
     * @param addressId 收货地址id
     */
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    /**
     * 获取提货订单号
     *
     * @return delivery_no - 提货订单号
     */
    public String getDeliveryNo() {
        return deliveryNo;
    }

    /**
     * 设置提货订单号
     *
     * @param deliveryNo 提货订单号
     */
    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    /**
     * 获取运单号
     *
     * @return logistics_no - 运单号
     */
    public String getLogisticsNo() {
        return logisticsNo;
    }

    /**
     * 设置运单号
     *
     * @param logisticsNo 运单号
     */
    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    /**
     * 获取提货订单状态(10:待审核；20:代发货；30:已发货；40:已收货)
     *
     * @return delivery_status - 提货订单状态(10:待审核；20:代发货；30:已发货；40:已收货)
     */
    public Integer getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * 设置提货订单状态(10:待审核；20:代发货；30:已发货；40:已收货)
     *
     * @param deliveryStatus 提货订单状态(10:待审核；20:代发货；30:已发货；40:已收货)
     */
    public void setDeliveryStatus(Integer deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    /**
     * 获取申请提货时间
     *
     * @return delivery_time - 申请提货时间
     */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * 设置申请提货时间
     *
     * @param deliveryTime 申请提货时间
     */
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * 获取审核时间
     *
     * @return audit_time - 审核时间
     */
    public Date getAuditTime() {
        return auditTime;
    }

    /**
     * 设置审核时间
     *
     * @param auditTime 审核时间
     */
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
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
     * 获取收货时间
     *
     * @return receive_time - 收货时间
     */
    public Date getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置收货时间
     *
     * @param receiveTime 收货时间
     */
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**
     * 获取物流类型
     *
     * @return logistics_type - 物流类型
     */
    public String getLogisticsType() {
        return logisticsType;
    }

    /**
     * 设置物流类型
     *
     * @param logisticsType 物流类型
     */
    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
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
     * 获取仓储费
     *
     * @return storage_charge - 仓储费
     */
    public Long getStorageCharge() {
        return storageCharge;
    }

    /**
     * 设置仓储费
     *
     * @param storageCharge 仓储费
     */
    public void setStorageCharge(Long storageCharge) {
        this.storageCharge = storageCharge;
    }

    /**
     * 获取出仓费
     *
     * @return clearance_charge - 出仓费
     */
    public Long getClearanceCharge() {
        return clearanceCharge;
    }

    /**
     * 设置出仓费
     *
     * @param clearanceCharge 出仓费
     */
    public void setClearanceCharge(Long clearanceCharge) {
        this.clearanceCharge = clearanceCharge;
    }

    /**
     * 获取运费
     *
     * @return freight - 运费
     */
    public Long getFreight() {
        return freight;
    }

    /**
     * 设置运费
     *
     * @param freight 运费
     */
    public void setFreight(Long freight) {
        this.freight = freight;
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
     * 获取提货数量
     *
     * @return delivery_count - 提货数量
     */
    public Integer getDeliveryCount() {
        return deliveryCount;
    }

    /**
     * 设置提货数量
     *
     * @param deliveryCount 提货数量
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

    /**
     * 获取持仓商品类型（main=本票,match=配票）
     *
     * @return hold_type - 持仓商品类型（main=本票,match=配票）
     */
    public String getHoldType() {
        return holdType;
    }

    /**
     * 设置持仓商品类型（main=本票,match=配票）
     *
     * @param holdType 持仓商品类型（main=本票,match=配票）
     */
    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getProductTradeNo() {
        return productTradeNo;
    }

    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    public String getProductTradeName() {
        return productTradeName;
    }

    public void setProductTradeName(String productTradeName) {
        this.productTradeName = productTradeName;
    }

    public BigDecimal getIssuePrice() {
        return issuePrice;
    }

    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }
}