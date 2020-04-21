package com.baibei.hengjia.admin.modules.tradingQuery.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_hold_total")
public class HoldTotal {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 商品总量
     */
    @Column(name = "total_count")
    private BigDecimal totalCount;

    /**
     * 商品冻结数量
     */
    @Column(name = "frozen_count")
    private BigDecimal frozenCount;

    /**
     * 可卖商品数量
     */
    @Column(name = "can_sell_count")
    private BigDecimal canSellCount;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 持仓单类型，main=本票，match=配票
     */
    @Column(name = "hold_type")
    private String holdType;

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
     * 获取交易商编码
     *
     * @return customer_no - 交易商编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置交易商编码
     *
     * @param customerNo 交易商编码
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
     * 获取商品总量
     *
     * @return total_count - 商品总量
     */
    public BigDecimal getTotalCount() {
        return totalCount;
    }

    /**
     * 设置商品总量
     *
     * @param totalCount 商品总量
     */
    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取商品冻结数量
     *
     * @return frozen_count - 商品冻结数量
     */
    public BigDecimal getFrozenCount() {
        return frozenCount;
    }

    /**
     * 设置商品冻结数量
     *
     * @param frozenCount 商品冻结数量
     */
    public void setFrozenCount(BigDecimal frozenCount) {
        this.frozenCount = frozenCount;
    }

    /**
     * 获取可卖商品数量
     *
     * @return can_sell_count - 可卖商品数量
     */
    public BigDecimal getCanSellCount() {
        return canSellCount;
    }

    /**
     * 设置可卖商品数量
     *
     * @param canSellCount 可卖商品数量
     */
    public void setCanSellCount(BigDecimal canSellCount) {
        this.canSellCount = canSellCount;
    }

    /**
     * 获取成本价
     *
     * @return cost - 成本价
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本价
     *
     * @param cost 成本价
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取持仓单类型，main=本票，match=配票
     *
     * @return hold_type - 持仓单类型，main=本票，match=配票
     */
    public String getHoldType() {
        return holdType;
    }

    /**
     * 设置持仓单类型，main=本票，match=配票
     *
     * @param holdType 持仓单类型，main=本票，match=配票
     */
    public void setHoldType(String holdType) {
        this.holdType = holdType;
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