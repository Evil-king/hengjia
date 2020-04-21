package com.baibei.hengjia.admin.modules.tradingQuery.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_deal_order")
public class DealOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 成交单单号
     */
    @Column(name = "deal_no")
    private String dealNo;

    /**
     * 关联委托单ID
     */
    @Column(name = "entrust_id")
    private Long entrustId;

    /**
     * 冗余商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 买方交易商编码
     */
    @Column(name = "buy_customer_no")
    private String buyCustomerNo;

    /**
     * 卖方交易商编码
     */
    @Column(name = "sell_customer_no")
    private String sellCustomerNo;

    /**
     * 买方手续费
     */
    @Column(name = "buy_fee")
    private BigDecimal buyFee;

    /**
     * 卖方手续费
     */
    @Column(name = "sell_fee")
    private BigDecimal sellFee;

    /**
     * 盈亏，配票卖出才有
     */
    private BigDecimal profit;

    /**
     * 采购转款，配票卖出才有
     */
    private BigDecimal integral;

    /**
     * 成交类型，buy=摘牌买入，sell=摘牌卖出，plan=配售
     */
    private String type;

    /**
     * 摘牌卖出类型，main=本票，match=配票
     */
    @Column(name = "hold_type")
    private String holdType;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private Integer count;

    /**
     * 成交时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 来源
     */
    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
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
     * 获取成交单单号
     *
     * @return deal_no - 成交单单号
     */
    public String getDealNo() {
        return dealNo;
    }

    /**
     * 设置成交单单号
     *
     * @param dealNo 成交单单号
     */
    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
    }

    /**
     * 获取关联委托单ID
     *
     * @return entrust_id - 关联委托单ID
     */
    public Long getEntrustId() {
        return entrustId;
    }

    /**
     * 设置关联委托单ID
     *
     * @param entrustId 关联委托单ID
     */
    public void setEntrustId(Long entrustId) {
        this.entrustId = entrustId;
    }

    /**
     * 获取冗余商品交易编码
     *
     * @return product_trade_no - 冗余商品交易编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置冗余商品交易编码
     *
     * @param productTradeNo 冗余商品交易编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取买方交易商编码
     *
     * @return buy_customer_no - 买方交易商编码
     */
    public String getBuyCustomerNo() {
        return buyCustomerNo;
    }

    /**
     * 设置买方交易商编码
     *
     * @param buyCustomerNo 买方交易商编码
     */
    public void setBuyCustomerNo(String buyCustomerNo) {
        this.buyCustomerNo = buyCustomerNo;
    }

    /**
     * 获取卖方交易商编码
     *
     * @return sell_customer_no - 卖方交易商编码
     */
    public String getSellCustomerNo() {
        return sellCustomerNo;
    }

    /**
     * 设置卖方交易商编码
     *
     * @param sellCustomerNo 卖方交易商编码
     */
    public void setSellCustomerNo(String sellCustomerNo) {
        this.sellCustomerNo = sellCustomerNo;
    }

    /**
     * 获取买方手续费
     *
     * @return buy_fee - 买方手续费
     */
    public BigDecimal getBuyFee() {
        return buyFee;
    }

    /**
     * 设置买方手续费
     *
     * @param buyFee 买方手续费
     */
    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }

    /**
     * 获取卖方手续费
     *
     * @return sell_fee - 卖方手续费
     */
    public BigDecimal getSellFee() {
        return sellFee;
    }

    /**
     * 设置卖方手续费
     *
     * @param sellFee 卖方手续费
     */
    public void setSellFee(BigDecimal sellFee) {
        this.sellFee = sellFee;
    }

    /**
     * 获取盈亏，配票卖出才有
     *
     * @return profit - 盈亏，配票卖出才有
     */
    public BigDecimal getProfit() {
        return profit;
    }

    /**
     * 设置盈亏，配票卖出才有
     *
     * @param profit 盈亏，配票卖出才有
     */
    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    /**
     * 获取采购转款，配票卖出才有
     *
     * @return integral - 采购转款，配票卖出才有
     */
    public BigDecimal getIntegral() {
        return integral;
    }

    /**
     * 设置采购转款，配票卖出才有
     *
     * @param integral 采购转款，配票卖出才有
     */
    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    /**
     * 获取成交类型，buy=摘牌买入，sell=摘牌卖出，plan=配售
     *
     * @return type - 成交类型，buy=摘牌买入，sell=摘牌卖出，plan=配售
     */
    public String getType() {
        return type;
    }

    /**
     * 设置成交类型，buy=摘牌买入，sell=摘牌卖出，plan=配售
     *
     * @param type 成交类型，buy=摘牌买入，sell=摘牌卖出，plan=配售
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取摘牌卖出类型，main=本票，match=配票
     *
     * @return hold_type - 摘牌卖出类型，main=本票，match=配票
     */
    public String getHoldType() {
        return holdType;
    }

    /**
     * 设置摘牌卖出类型，main=本票，match=配票
     *
     * @param holdType 摘牌卖出类型，main=本票，match=配票
     */
    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    /**
     * 获取成交价格
     *
     * @return price - 成交价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置成交价格
     *
     * @param price 成交价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取成交数量
     *
     * @return count - 成交数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置成交数量
     *
     * @param count 成交数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取成交时间
     *
     * @return create_time - 成交时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置成交时间
     *
     * @param createTime 成交时间
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
     * 获取是否删除(1:正常，0:删除)
     *
     * @return flag - 是否删除(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(1:正常，0:删除)
     *
     * @param flag 是否删除(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}