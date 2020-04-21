package com.baibei.hengjia.api.modules.match.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_ph_buymatch_user")
public class BuymatchUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次号
     */
    @Column(name = "batch_no")
    private String batchNo;

    /**
     * 成交单号
     */
    @Column(name = "deal_no")
    private String dealNo;

    /**
     * 用户编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 配货数量
     */
    @Column(name = "match_num")
    private Long matchNum;

    /**
     * 状态（run=已执行过；unrun=未执行过）
     */
    private String status;

    /**
     * 执行类型（send=送货；plan=配货）
     */
    @Column(name = "operate_type")
    private String operateType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

    private Byte flag;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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
     * 获取批次号
     *
     * @return batch_no - 批次号
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 设置批次号
     *
     * @param batchNo 批次号
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 获取成交单号
     *
     * @return deal_no - 成交单号
     */
    public String getDealNo() {
        return dealNo;
    }

    /**
     * 设置成交单号
     *
     * @param dealNo 成交单号
     */
    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
    }

    /**
     * 获取用户编码
     *
     * @return customer_no - 用户编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置用户编码
     *
     * @param customerNo 用户编码
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
     * 获取配货数量
     *
     * @return match_num - 配货数量
     */
    public Long getMatchNum() {
        return matchNum;
    }

    /**
     * 设置配货数量
     *
     * @param matchNum 配货数量
     */
    public void setMatchNum(Long matchNum) {
        this.matchNum = matchNum;
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

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}