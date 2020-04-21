package com.baibei.hengjia.api.modules.match.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ph_buymatch_log")
public class BuymatchLog {
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
     * 买入数量
     */
    @Column(name = "match_num")
    private Long matchNum;


    /**
     * 折扣价
     */
    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 类型（common=正常；offset=补货<前几次配货失败>）
     */
    private String type;

    /**
     * 状态（success=成功；fail=失败）
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    @Column(name = "create_time")
    private Date createTime;

    /**
     * CURRENT_TIMESTAMP
     */
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
     * 获取买入数量
     *
     * @return match_num - 买入数量
     */
    public Long getMatchNum() {
        return matchNum;
    }

    /**
     * 设置买入数量
     *
     * @param matchNum 买入数量
     */
    public void setMatchNum(Long matchNum) {
        this.matchNum = matchNum;
    }


    /**
     * 获取折扣价
     *
     * @return discount_price - 折扣价
     */
    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    /**
     * 设置折扣价
     *
     * @param discountPrice 折扣价
     */
    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
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
     * 获取类型（common=正常；offset=补货<前几次配货失败>）
     *
     * @return type - 类型（common=正常；offset=补货<前几次配货失败>）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置类型（common=正常；offset=补货<前几次配货失败>）
     *
     * @param type 类型（common=正常；offset=补货<前几次配货失败>）
     */
    public void setType(String type) {
        this.type = type;
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
     * 获取CURRENT_TIMESTAMP
     *
     * @return modify_time - CURRENT_TIMESTAMP
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置CURRENT_TIMESTAMP
     *
     * @param modifyTime CURRENT_TIMESTAMP
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}