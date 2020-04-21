package com.baibei.hengjia.admin.modules.tradingQuery.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_entrust_order")
public class EntrustOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 委托单单号
     */
    @Column(name = "entrust_no")
    private String entrustNo;

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
     * 委托时间
     */
    @Column(name = "entrust_time")
    private Date entrustTime;

    /**
     * 委托方向，buy=挂牌买入，sell=挂牌卖出
     */
    private String direction;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 委托数量
     */
    @Column(name = "entrust_count")
    private Integer entrustCount;

    /**
     * 成交数量
     */
    @Column(name = "deal_count")
    private Integer dealCount;

    /**
     * 撤销数量
     */
    @Column(name = "revoke_count")
    private Integer revokeCount;

    /**
     * 委托结果，wait_deal=待成交，all_deal=全部成交，some_deal=部分成交，revoke=撤单
     */
    private String result;

    /**
     * 成交单状态，init=初始化，ok=委托成功，fail=委托失败
     */
    private String status;

    /**
     * 委托失败原因
     */
    private String reason;

    /**
     * 挂牌买入的手续费
     */
    private BigDecimal fee;

    /**
     * 挂牌卖出类型，main=本票，match=配票
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
     * 获取委托单单号
     *
     * @return entrust_no - 委托单单号
     */
    public String getEntrustNo() {
        return entrustNo;
    }

    /**
     * 设置委托单单号
     *
     * @param entrustNo 委托单单号
     */
    public void setEntrustNo(String entrustNo) {
        this.entrustNo = entrustNo;
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
     * 获取委托时间
     *
     * @return entrust_time - 委托时间
     */
    public Date getEntrustTime() {
        return entrustTime;
    }

    /**
     * 设置委托时间
     *
     * @param entrustTime 委托时间
     */
    public void setEntrustTime(Date entrustTime) {
        this.entrustTime = entrustTime;
    }

    /**
     * 获取委托方向，buy=挂牌买入，sell=挂牌卖出
     *
     * @return direction - 委托方向，buy=挂牌买入，sell=挂牌卖出
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 设置委托方向，buy=挂牌买入，sell=挂牌卖出
     *
     * @param direction 委托方向，buy=挂牌买入，sell=挂牌卖出
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * 获取委托价格
     *
     * @return price - 委托价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置委托价格
     *
     * @param price 委托价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取委托数量
     *
     * @return entrust_count - 委托数量
     */
    public Integer getEntrustCount() {
        return entrustCount;
    }

    /**
     * 设置委托数量
     *
     * @param entrustCount 委托数量
     */
    public void setEntrustCount(Integer entrustCount) {
        this.entrustCount = entrustCount;
    }

    /**
     * 获取成交数量
     *
     * @return deal_count - 成交数量
     */
    public Integer getDealCount() {
        return dealCount;
    }

    /**
     * 设置成交数量
     *
     * @param dealCount 成交数量
     */
    public void setDealCount(Integer dealCount) {
        this.dealCount = dealCount;
    }

    /**
     * 获取撤销数量
     *
     * @return revoke_count - 撤销数量
     */
    public Integer getRevokeCount() {
        return revokeCount;
    }

    /**
     * 设置撤销数量
     *
     * @param revokeCount 撤销数量
     */
    public void setRevokeCount(Integer revokeCount) {
        this.revokeCount = revokeCount;
    }

    /**
     * 获取委托结果，wait_deal=待成交，all_deal=全部成交，some_deal=部分成交，revoke=撤单
     *
     * @return result - 委托结果，wait_deal=待成交，all_deal=全部成交，some_deal=部分成交，revoke=撤单
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置委托结果，wait_deal=待成交，all_deal=全部成交，some_deal=部分成交，revoke=撤单
     *
     * @param result 委托结果，wait_deal=待成交，all_deal=全部成交，some_deal=部分成交，revoke=撤单
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 获取成交单状态，init=初始化，ok=委托成功，fail=委托失败
     *
     * @return status - 成交单状态，init=初始化，ok=委托成功，fail=委托失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置成交单状态，init=初始化，ok=委托成功，fail=委托失败
     *
     * @param status 成交单状态，init=初始化，ok=委托成功，fail=委托失败
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取委托失败原因
     *
     * @return reason - 委托失败原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置委托失败原因
     *
     * @param reason 委托失败原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取挂牌买入的手续费
     *
     * @return fee - 挂牌买入的手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置挂牌买入的手续费
     *
     * @param fee 挂牌买入的手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取挂牌卖出类型，main=本票，match=配票
     *
     * @return hold_type - 挂牌卖出类型，main=本票，match=配票
     */
    public String getHoldType() {
        return holdType;
    }

    /**
     * 设置挂牌卖出类型，main=本票，match=配票
     *
     * @param holdType 挂牌卖出类型，main=本票，match=配票
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