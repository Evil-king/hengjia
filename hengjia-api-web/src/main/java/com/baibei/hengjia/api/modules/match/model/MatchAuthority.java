package com.baibei.hengjia.api.modules.match.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ph_match_authority")
public class MatchAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_no")
    private String customerNo;

    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 配货权
     */
    @Column(name = "match_authority")
    private Integer matchAuthority;

    /**
     * 状态（usable=可用，disabled=不可用）
     */
    private String status;

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
     * 获取配货权
     *
     * @return match_authority - 配货权
     */
    public Integer getMatchAuthority() {
        return matchAuthority;
    }

    /**
     * 设置配货权
     *
     * @param matchAuthority 配货权
     */
    public void setMatchAuthority(Integer matchAuthority) {
        this.matchAuthority = matchAuthority;
    }

    /**
     * 获取状态（usable=可用，disabled=不可用）
     *
     * @return status - 状态（usable=可用，disabled=不可用）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（usable=可用，disabled=不可用）
     *
     * @param status 状态（usable=可用，disabled=不可用）
     */
    public void setStatus(String status) {
        this.status = status;
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