package com.baibei.hengjia.api.modules.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_auto_config")
public class AutoConfig {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 冗余交易商品名称
     */
    @Column(name = "product_trade_name")
    private String productTradeName;

    /**
     * 开启状态，on=开启，off=关闭
     */
    private String status;

    /**
     * 商品达到该价格时自动买入
     */
    @Column(name = "auto_buy_price")
    private BigDecimal autoBuyPrice;

    /**
     * 自动买入的数量
     */
    @Column(name = "auto_buy_count")
    private Integer autoBuyCount;

    /**
     * 商品达到该价格时自动卖出
     */
    @Column(name = "auto_sell_price")
    private BigDecimal autoSellPrice;

    /**
     * 自动卖出的数量
     */
    @Column(name = "auto_sell_count")
    private Integer autoSellCount;

    /**
     * 截止时间
     */
    @Column(name = "closing_time")
    private Date closingTime;

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
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

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
     * 获取客户编号
     *
     * @return customer_no - 客户编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置客户编号
     *
     * @param customerNo 客户编号
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
     * 获取冗余交易商品名称
     *
     * @return product_trade_name - 冗余交易商品名称
     */
    public String getProductTradeName() {
        return productTradeName;
    }

    /**
     * 设置冗余交易商品名称
     *
     * @param productTradeName 冗余交易商品名称
     */
    public void setProductTradeName(String productTradeName) {
        this.productTradeName = productTradeName;
    }

    /**
     * 获取开启状态，on=开启，off=关闭
     *
     * @return status - 开启状态，on=开启，off=关闭
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置开启状态，on=开启，off=关闭
     *
     * @param status 开启状态，on=开启，off=关闭
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取商品达到该价格时自动买入
     *
     * @return auto_buy_price - 商品达到该价格时自动买入
     */
    public BigDecimal getAutoBuyPrice() {
        return autoBuyPrice;
    }

    /**
     * 设置商品达到该价格时自动买入
     *
     * @param autoBuyPrice 商品达到该价格时自动买入
     */
    public void setAutoBuyPrice(BigDecimal autoBuyPrice) {
        this.autoBuyPrice = autoBuyPrice;
    }

    /**
     * 获取自动买入的数量
     *
     * @return auto_buy_count - 自动买入的数量
     */
    public Integer getAutoBuyCount() {
        return autoBuyCount;
    }

    /**
     * 设置自动买入的数量
     *
     * @param autoBuyCount 自动买入的数量
     */
    public void setAutoBuyCount(Integer autoBuyCount) {
        this.autoBuyCount = autoBuyCount;
    }

    /**
     * 获取商品达到该价格时自动卖出
     *
     * @return auto_sell_price - 商品达到该价格时自动卖出
     */
    public BigDecimal getAutoSellPrice() {
        return autoSellPrice;
    }

    /**
     * 设置商品达到该价格时自动卖出
     *
     * @param autoSellPrice 商品达到该价格时自动卖出
     */
    public void setAutoSellPrice(BigDecimal autoSellPrice) {
        this.autoSellPrice = autoSellPrice;
    }

    /**
     * 获取自动卖出的数量
     *
     * @return auto_sell_count - 自动卖出的数量
     */
    public Integer getAutoSellCount() {
        return autoSellCount;
    }

    /**
     * 设置自动卖出的数量
     *
     * @param autoSellCount 自动卖出的数量
     */
    public void setAutoSellCount(Integer autoSellCount) {
        this.autoSellCount = autoSellCount;
    }

    /**
     * 获取截止时间
     *
     * @return closing_time - 截止时间
     */
    public Date getClosingTime() {
        return closingTime;
    }

    /**
     * 设置截止时间
     *
     * @param closingTime 截止时间
     */
    public void setClosingTime(Date closingTime) {
        this.closingTime = closingTime;
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