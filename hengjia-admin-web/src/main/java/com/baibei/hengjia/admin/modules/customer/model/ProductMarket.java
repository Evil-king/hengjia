package com.baibei.hengjia.admin.modules.customer.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_product_market")
public class ProductMarket {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 货号，商品的唯一编码
     */
    @Column(name = "spu_no")
    private String spuNo;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 商品交易名称
     */
    @Column(name = "product_trade_name")
    private String productTradeName;

    /**
     * 发行价
     */
    @Column(name = "issue_price")
    private BigDecimal issuePrice;

    /**
     * 最小提货量
     */
    @Column(name = "min_take")
    private Integer minTake;

    /**
     * 单位
     */
    private String units;

    /**
     * 最高报价
     */
    private BigDecimal high;

    /**
     * 最低报价
     */
    private BigDecimal low;

    /**
     * 上市日期
     */
    @Column(name = "market_time")
    private Date marketTime;

    /**
     * 交易日期
     */
    @Column(name = "trade_time")
    private Date tradeTime;

    /**
     * T+N天
     */
    @Column(name = "frozen_day")
    private Integer frozenDay;

    /**
     * 冗余归属挂牌商
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 配票<货>规则（deliveryMatch=提货配票，buyMatch=买入配货）
     */
    @Column(name = "match_rule")
    private String matchRule;

    /**
     * 交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；onmarket=已上市；trading=交易中；stop=停盘；exit=退市
     */
    @Column(name = "trade_status")
    private String tradeStatus;

    /**
     * 最小提货数量
     */
    @Column(name = "delivery_num")
    private Integer deliveryNum;

    /**
     * 创建人
     */
    private String creator;

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
     * 状态(1:正常，0:禁用)
     */
    private Byte flag;

    /**
     * 配票规则（match:配货 vouchers:代金券）
     */
    @Column(name = "ticket_rule")
    private String ticketRule;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取货号，商品的唯一编码
     *
     * @return spu_no - 货号，商品的唯一编码
     */
    public String getSpuNo() {
        return spuNo;
    }

    /**
     * 设置货号，商品的唯一编码
     *
     * @param spuNo 货号，商品的唯一编码
     */
    public void setSpuNo(String spuNo) {
        this.spuNo = spuNo;
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
     * 获取商品交易名称
     *
     * @return product_trade_name - 商品交易名称
     */
    public String getProductTradeName() {
        return productTradeName;
    }

    /**
     * 设置商品交易名称
     *
     * @param productTradeName 商品交易名称
     */
    public void setProductTradeName(String productTradeName) {
        this.productTradeName = productTradeName;
    }

    /**
     * 获取发行价
     *
     * @return issue_price - 发行价
     */
    public BigDecimal getIssuePrice() {
        return issuePrice;
    }

    /**
     * 设置发行价
     *
     * @param issuePrice 发行价
     */
    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }

    /**
     * 获取最小提货量
     *
     * @return min_take - 最小提货量
     */
    public Integer getMinTake() {
        return minTake;
    }

    /**
     * 设置最小提货量
     *
     * @param minTake 最小提货量
     */
    public void setMinTake(Integer minTake) {
        this.minTake = minTake;
    }

    /**
     * 获取单位
     *
     * @return units - 单位
     */
    public String getUnits() {
        return units;
    }

    /**
     * 设置单位
     *
     * @param units 单位
     */
    public void setUnits(String units) {
        this.units = units;
    }

    /**
     * 获取最高报价
     *
     * @return high - 最高报价
     */
    public BigDecimal getHigh() {
        return high;
    }

    /**
     * 设置最高报价
     *
     * @param high 最高报价
     */
    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    /**
     * 获取最低报价
     *
     * @return low - 最低报价
     */
    public BigDecimal getLow() {
        return low;
    }

    /**
     * 设置最低报价
     *
     * @param low 最低报价
     */
    public void setLow(BigDecimal low) {
        this.low = low;
    }

    /**
     * 获取上市日期
     *
     * @return market_time - 上市日期
     */
    public Date getMarketTime() {
        return marketTime;
    }

    /**
     * 设置上市日期
     *
     * @param marketTime 上市日期
     */
    public void setMarketTime(Date marketTime) {
        this.marketTime = marketTime;
    }

    /**
     * 获取交易日期
     *
     * @return trade_time - 交易日期
     */
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * 设置交易日期
     *
     * @param tradeTime 交易日期
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    /**
     * 获取T+N天
     *
     * @return frozen_day - T+N天
     */
    public Integer getFrozenDay() {
        return frozenDay;
    }

    /**
     * 设置T+N天
     *
     * @param frozenDay T+N天
     */
    public void setFrozenDay(Integer frozenDay) {
        this.frozenDay = frozenDay;
    }

    /**
     * 获取冗余归属挂牌商
     *
     * @return member_no - 冗余归属挂牌商
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置冗余归属挂牌商
     *
     * @param memberNo 冗余归属挂牌商
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取配票<货>规则（deliveryMatch=提货配票，buyMatch=买入配货）
     *
     * @return match_rule - 配票<货>规则（deliveryMatch=提货配票，buyMatch=买入配货）
     */
    public String getMatchRule() {
        return matchRule;
    }

    /**
     * 设置配票<货>规则（deliveryMatch=提货配票，buyMatch=买入配货）
     *
     * @param matchRule 配票<货>规则（deliveryMatch=提货配票，buyMatch=买入配货）
     */
    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    /**
     * 获取交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；onmarket=已上市；trading=交易中；stop=停盘；exit=退市
     *
     * @return trade_status - 交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；onmarket=已上市；trading=交易中；stop=停盘；exit=退市
     */
    public String getTradeStatus() {
        return tradeStatus;
    }

    /**
     * 设置交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；onmarket=已上市；trading=交易中；stop=停盘；exit=退市
     *
     * @param tradeStatus 交易状态，submit=已提交（创建完还未审核通过）；wait=待上市；onmarket=已上市；trading=交易中；stop=停盘；exit=退市
     */
    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    /**
     * 获取最小提货数量
     *
     * @return delivery_num - 最小提货数量
     */
    public Integer getDeliveryNum() {
        return deliveryNum;
    }

    /**
     * 设置最小提货数量
     *
     * @param deliveryNum 最小提货数量
     */
    public void setDeliveryNum(Integer deliveryNum) {
        this.deliveryNum = deliveryNum;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
     * 获取配票规则（match:配货 vouchers:代金券）
     *
     * @return ticket_rule - 配票规则（match:配货 vouchers:代金券）
     */
    public String getTicketRule() {
        return ticketRule;
    }

    /**
     * 设置配票规则（match:配货 vouchers:代金券）
     *
     * @param ticketRule 配票规则（match:配货 vouchers:代金券）
     */
    public void setTicketRule(String ticketRule) {
        this.ticketRule = ticketRule;
    }
}