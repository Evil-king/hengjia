package com.baibei.hengjia.api.modules.match.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ph_offset_deliveryticket_record")
public class OffsetDeliveryticketRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_no")
    private String recordNo;

    /**
     * 关联单号
     */
    @Column(name = "order_no")
    private String orderNo;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 原始剩余需要被消费的配货权数
     */
    private Integer authority;

    /**
     * 本次消费的配货权数
     */
    @Column(name = "change_amount")
    private Integer changeAmount;

    /**
     * 剩余配货权数
     */
    @Column(name = "remain_amount")
    private Integer remainAmount;

    /**
     * 备注
     */
    private String remark;

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
     * @return record_no
     */
    public String getRecordNo() {
        return recordNo;
    }

    /**
     * @param recordNo
     */
    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    /**
     * 获取关联单号
     *
     * @return order_no - 关联单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置关联单号
     *
     * @param orderNo 关联单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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
     * 获取原始剩余需要被消费的配货权数
     *
     * @return authority - 原始剩余需要被消费的配货权数
     */
    public Integer getAuthority() {
        return authority;
    }

    /**
     * 设置原始剩余需要被消费的配货权数
     *
     * @param authority 原始剩余需要被消费的配货权数
     */
    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    /**
     * 获取本次消费的配货权数
     *
     * @return change_amount - 本次消费的配货权数
     */
    public Integer getChangeAmount() {
        return changeAmount;
    }

    /**
     * 设置本次消费的配货权数
     *
     * @param changeAmount 本次消费的配货权数
     */
    public void setChangeAmount(Integer changeAmount) {
        this.changeAmount = changeAmount;
    }

    /**
     * 获取剩余配货权数
     *
     * @return remain_amount - 剩余配货权数
     */
    public Integer getRemainAmount() {
        return remainAmount;
    }

    /**
     * 设置剩余配货权数
     *
     * @param remainAmount 剩余配货权数
     */
    public void setRemainAmount(Integer remainAmount) {
        this.remainAmount = remainAmount;
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