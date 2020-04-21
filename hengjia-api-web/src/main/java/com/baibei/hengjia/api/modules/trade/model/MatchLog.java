package com.baibei.hengjia.api.modules.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_match_log")
public class MatchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 配票单号
     */
    @Column(name = "match_no")
    private String matchNo;

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
     * 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票)
     */
    @Column(name = "match_type")
    private String matchType;

    /**
     * 配票数量
     */
    @Column(name = "match_num")
    private BigDecimal matchNum;

    /**
     * 成功配票的数量
     */
    @Column(name = "match_success_num")
    private BigDecimal matchSuccessNum;

    /**
     * 配票金额（用户应扣除的金额）= 配票成本+手续费
     */
    @Column(name = "match_money")
    private BigDecimal matchMoney;
    /**
     * 配票成本
     */
    private BigDecimal cost;
    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 时间段
     */
    private Date period;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    @Column(name = "match_status")
    private String matchStatus;

    /**
     * 是否有效（1：有效；0：无效）
     */
    private Integer flag;

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
     * 获取配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票)
     *
     * @return match_type - 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票)
     */
    public String getMatchType() {
        return matchType;
    }

    /**
     * 设置配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票)
     *
     * @param matchType 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票)
     */
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public BigDecimal getMatchNum() {
        return matchNum;
    }

    public void setMatchNum(BigDecimal matchNum) {
        this.matchNum = matchNum;
    }

    public BigDecimal getMatchSuccessNum() {
        return matchSuccessNum;
    }

    public void setMatchSuccessNum(BigDecimal matchSuccessNum) {
        this.matchSuccessNum = matchSuccessNum;
    }

    public BigDecimal getMatchMoney() {
        return matchMoney;
    }

    public void setMatchMoney(BigDecimal matchMoney) {
        this.matchMoney = matchMoney;
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
     * 获取更新时间
     *
     * @return modify_time - 更新时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }


    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    /**
     * 获取是否有效（1：有效；0：无效）
     *
     * @return flag - 是否有效（1：有效；0：无效）
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置是否有效（1：有效；0：无效）
     *
     * @param flag 是否有效（1：有效；0：无效）
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMatchNo() {
        return matchNo;
    }

    public void setMatchNo(String matchNo) {
        this.matchNo = matchNo;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}