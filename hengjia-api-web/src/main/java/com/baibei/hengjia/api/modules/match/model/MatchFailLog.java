package com.baibei.hengjia.api.modules.match.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ph_match_fail_log")
public class MatchFailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 配货数量
     */
    @Column(name = "match_num")
    private Long matchNum;

    /**
     * 失败次数
     */
    @Column(name = "fail_count")
    private Integer failCount;

    /**
     * 失败类型（balanceLimit=余额不足；authorityLimit=配货权不足）
     */
    @Column(name = "fail_type")
    private String failType;

    /**
     * 状态（deal:已执行；wait:待执行；destory:已失效）
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
     * 获取失败次数
     *
     * @return fail_count - 失败次数
     */
    public Integer getFailCount() {
        return failCount;
    }

    /**
     * 设置失败次数
     *
     * @param failCount 失败次数
     */
    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    /**
     * 获取状态（deal:已执行；wait:待执行；destory:已失效）
     *
     * @return status - 状态（deal:已执行；wait:待执行；destory:已失效）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（deal:已执行；wait:待执行；destory:已失效）
     *
     * @param status 状态（deal:已执行；wait:待执行；destory:已失效）
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

    public String getFailType() {
        return failType;
    }

    public void setFailType(String failType) {
        this.failType = failType;
    }
}