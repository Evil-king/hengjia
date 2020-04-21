package com.baibei.hengjia.admin.modules.tradingQuery.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_hold_details")
public class HoldDetails {
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
     * 商品原始数量
     */
    @Column(name = "original_count")
    private BigDecimal originalCount;

    /**
     * 商品冻结数量
     */
    @Column(name = "frozen_count")
    private BigDecimal frozenCount;

    /**
     * 商品剩余数量
     */
    @Column(name = "remaind_count")
    private BigDecimal remaindCount;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 可交易日期
     */
    @Column(name = "trade_time")
    private Date tradeTime;

    /**
     * 持有流水号
     */
    @Column(name = "hold_no")
    private String holdNo;

    /**
     * 持有时间
     */
    @Column(name = "hold_time")
    private Date holdTime;

    /**
     * 持有来源，deal=摘牌成交、plan=配售、transfer=非交易过户
     */
    private String resource;

    /**
     * 是否已扫描(1:已扫描，0:未扫描)
     */
    private Byte scanner;

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
     * 获取商品原始数量
     *
     * @return original_count - 商品原始数量
     */
    public BigDecimal getOriginalCount() {
        return originalCount;
    }

    /**
     * 设置商品原始数量
     *
     * @param originalCount 商品原始数量
     */
    public void setOriginalCount(BigDecimal originalCount) {
        this.originalCount = originalCount;
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
     * 获取商品剩余数量
     *
     * @return remaind_count - 商品剩余数量
     */
    public BigDecimal getRemaindCount() {
        return remaindCount;
    }

    /**
     * 设置商品剩余数量
     *
     * @param remaindCount 商品剩余数量
     */
    public void setRemaindCount(BigDecimal remaindCount) {
        this.remaindCount = remaindCount;
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
     * 获取可交易日期
     *
     * @return trade_time - 可交易日期
     */
    public Date getTradeTime() {
        return tradeTime;
    }

    /**
     * 设置可交易日期
     *
     * @param tradeTime 可交易日期
     */
    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    /**
     * 获取持有流水号
     *
     * @return hold_no - 持有流水号
     */
    public String getHoldNo() {
        return holdNo;
    }

    /**
     * 设置持有流水号
     *
     * @param holdNo 持有流水号
     */
    public void setHoldNo(String holdNo) {
        this.holdNo = holdNo;
    }

    /**
     * 获取持有时间
     *
     * @return hold_time - 持有时间
     */
    public Date getHoldTime() {
        return holdTime;
    }

    /**
     * 设置持有时间
     *
     * @param holdTime 持有时间
     */
    public void setHoldTime(Date holdTime) {
        this.holdTime = holdTime;
    }

    /**
     * 获取持有来源，deal=摘牌成交、plan=配售、transfer=非交易过户
     *
     * @return resource - 持有来源，deal=摘牌成交、plan=配售、transfer=非交易过户
     */
    public String getResource() {
        return resource;
    }

    /**
     * 设置持有来源，deal=摘牌成交、plan=配售、transfer=非交易过户
     *
     * @param resource 持有来源，deal=摘牌成交、plan=配售、transfer=非交易过户
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * 获取是否已扫描(1:已扫描，0:未扫描)
     *
     * @return scanner - 是否已扫描(1:已扫描，0:未扫描)
     */
    public Byte getScanner() {
        return scanner;
    }

    /**
     * 设置是否已扫描(1:已扫描，0:未扫描)
     *
     * @param scanner 是否已扫描(1:已扫描，0:未扫描)
     */
    public void setScanner(Byte scanner) {
        this.scanner = scanner;
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