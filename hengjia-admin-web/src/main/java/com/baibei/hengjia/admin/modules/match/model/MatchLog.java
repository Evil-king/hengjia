package com.baibei.hengjia.admin.modules.match.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_tra_match_log")
public class MatchLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:提货配票)
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
     * 配票金额（用户应扣除的金额）=成本+手续费
     */
    @Column(name = "match_money")
    private BigDecimal matchMoney;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    @Column(name = "match_status")
    private String matchStatus;

    /**
     * 时间段（年月日格式，和match_no字段做联合唯一索引）
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
     * 是否有效（1：有效；0：无效）
     */
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
     * @return match_no
     */
    public String getMatchNo() {
        return matchNo;
    }

    /**
     * @param matchNo
     */
    public void setMatchNo(String matchNo) {
        this.matchNo = matchNo;
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
     * 获取配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:提货配票)
     *
     * @return match_type - 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:提货配票)
     */
    public String getMatchType() {
        return matchType;
    }

    /**
     * 设置配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:提货配票)
     *
     * @param matchType 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:提货配票)
     */
    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    /**
     * 获取配票数量
     *
     * @return match_num - 配票数量
     */
    public BigDecimal getMatchNum() {
        return matchNum;
    }

    /**
     * 设置配票数量
     *
     * @param matchNum 配票数量
     */
    public void setMatchNum(BigDecimal matchNum) {
        this.matchNum = matchNum;
    }

    /**
     * 获取成功配票的数量
     *
     * @return match_success_num - 成功配票的数量
     */
    public BigDecimal getMatchSuccessNum() {
        return matchSuccessNum;
    }

    /**
     * 设置成功配票的数量
     *
     * @param matchSuccessNum 成功配票的数量
     */
    public void setMatchSuccessNum(BigDecimal matchSuccessNum) {
        this.matchSuccessNum = matchSuccessNum;
    }

    /**
     * 获取配票金额（用户应扣除的金额）=成本+手续费
     *
     * @return match_money - 配票金额（用户应扣除的金额）=成本+手续费
     */
    public BigDecimal getMatchMoney() {
        return matchMoney;
    }

    /**
     * 设置配票金额（用户应扣除的金额）=成本+手续费
     *
     * @param matchMoney 配票金额（用户应扣除的金额）=成本+手续费
     */
    public void setMatchMoney(BigDecimal matchMoney) {
        this.matchMoney = matchMoney;
    }

    /**
     * 获取成本
     *
     * @return cost - 成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本
     *
     * @param cost 成本
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
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
     * 获取配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     *
     * @return match_status - 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    public String getMatchStatus() {
        return matchStatus;
    }

    /**
     * 设置配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     *
     * @param matchStatus 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    /**
     * 获取时间段（年月日格式，和match_no字段做联合唯一索引）
     *
     * @return period - 时间段（年月日格式，和match_no字段做联合唯一索引）
     */
    public Date getPeriod() {
        return period;
    }

    /**
     * 设置时间段（年月日格式，和match_no字段做联合唯一索引）
     *
     * @param period 时间段（年月日格式，和match_no字段做联合唯一索引）
     */
    public void setPeriod(Date period) {
        this.period = period;
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

    /**
     * 获取是否有效（1：有效；0：无效）
     *
     * @return flag - 是否有效（1：有效；0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否有效（1：有效；0：无效）
     *
     * @param flag 是否有效（1：有效；0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}