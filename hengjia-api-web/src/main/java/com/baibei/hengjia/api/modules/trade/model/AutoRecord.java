package com.baibei.hengjia.api.modules.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_auto_record")
public class AutoRecord {
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
     * 关联委托单单号
     */
    @Column(name = "entrust_no")
    private String entrustNo;

    /**
     * 买卖类型，buy=买入，sell=卖出
     */
    private String type;

    /**
     * 价格达到
     */
    @Column(name = "auto_price")
    private BigDecimal autoPrice;

    /**
     * 买卖数量
     */
    @Column(name = "auto_count")
    private Integer autoCount;

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


    public String getEntrustNo() {
        return entrustNo;
    }

    public void setEntrustNo(String entrustNo) {
        this.entrustNo = entrustNo;
    }

    /**
     * 获取买卖类型，buy=买入，sell=卖出
     *
     * @return type - 买卖类型，buy=买入，sell=卖出
     */
    public String getType() {
        return type;
    }

    /**
     * 设置买卖类型，buy=买入，sell=卖出
     *
     * @param type 买卖类型，buy=买入，sell=卖出
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取价格达到
     *
     * @return auto_price - 价格达到
     */
    public BigDecimal getAutoPrice() {
        return autoPrice;
    }

    /**
     * 设置价格达到
     *
     * @param autoPrice 价格达到
     */
    public void setAutoPrice(BigDecimal autoPrice) {
        this.autoPrice = autoPrice;
    }

    /**
     * 获取买卖数量
     *
     * @return auto_count - 买卖数量
     */
    public Integer getAutoCount() {
        return autoCount;
    }

    /**
     * 设置买卖数量
     *
     * @param autoCount 买卖数量
     */
    public void setAutoCount(Integer autoCount) {
        this.autoCount = autoCount;
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