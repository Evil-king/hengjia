package com.baibei.hengjia.api.modules.match.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ph_offset_deliveryticket")
public class OffsetDeliveryticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 原始需要消费掉的配货权数
     */
    @Column(name = "orign_authority")
    private Integer orignAuthority;

    /**
     * 剩余需要被消费的配货权数（减到0时，就不再补扣）
     */
    @Column(name = "remain_authority")
    private Integer remainAuthority;

    /**
     * 已消费的配货权数
     */
    @Column(name = "comsumed_authority")
    private Integer comsumedAuthority;

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
     * 获取原始需要消费掉的配货权数
     *
     * @return orign_authority - 原始需要消费掉的配货权数
     */
    public Integer getOrignAuthority() {
        return orignAuthority;
    }

    /**
     * 设置原始需要消费掉的配货权数
     *
     * @param orignAuthority 原始需要消费掉的配货权数
     */
    public void setOrignAuthority(Integer orignAuthority) {
        this.orignAuthority = orignAuthority;
    }

    /**
     * 获取剩余需要被消费的配货权数（减到0时，就不再补扣）
     *
     * @return remain_authority - 剩余需要被消费的配货权数（减到0时，就不再补扣）
     */
    public Integer getRemainAuthority() {
        return remainAuthority;
    }

    /**
     * 设置剩余需要被消费的配货权数（减到0时，就不再补扣）
     *
     * @param remainAuthority 剩余需要被消费的配货权数（减到0时，就不再补扣）
     */
    public void setRemainAuthority(Integer remainAuthority) {
        this.remainAuthority = remainAuthority;
    }

    /**
     * 获取已消费的配货权数
     *
     * @return comsumed_authority - 已消费的配货权数
     */
    public Integer getComsumedAuthority() {
        return comsumedAuthority;
    }

    /**
     * 设置已消费的配货权数
     *
     * @param comsumedAuthority 已消费的配货权数
     */
    public void setComsumedAuthority(Integer comsumedAuthority) {
        this.comsumedAuthority = comsumedAuthority;
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
}