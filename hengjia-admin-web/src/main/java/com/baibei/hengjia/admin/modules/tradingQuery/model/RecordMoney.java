package com.baibei.hengjia.admin.modules.tradingQuery.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_record_money")
public class RecordMoney {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 流水号
     */
    @Column(name = "record_no")
    private String recordNo;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 状态:101：入金 102:出金 103：本票买入 104：本票卖出 105：配票买入 106：配票卖出 107：调账 108：出金手续费 109：出金手续费回退 110：出金回退 111：费用支付 112：费用补扣 113：手续费结算收入 114：采购专款结算收入
     */
    @Column(name = "trade_type")
    private Byte tradeType;

    /**
     * 变动后可提现金额
     */
    private BigDecimal withdraw;

    /**
     * 变动后余额
     */
    private BigDecimal balance;

    /**
     * 变动数额
     */
    @Column(name = "change_amount")
    private String changeAmount;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 收入或支出（1：支出2：收入）
     */
    private Byte retype;

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
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 状态（1：有效0：无效）
     */
    private Byte flag;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取流水号
     *
     * @return record_no - 流水号
     */
    public String getRecordNo() {
        return recordNo;
    }

    /**
     * 设置流水号
     *
     * @param recordNo 流水号
     */
    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
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
     * 获取状态:101：入金 102:出金 103：本票买入 104：本票卖出 105：配票买入 106：配票卖出 107：调账 108：出金手续费 109：出金手续费回退 110：出金回退 111：费用支付 112：费用补扣 113：手续费结算收入 114：采购专款结算收入
     *
     * @return trade_type - 状态:101：入金 102:出金 103：本票买入 104：本票卖出 105：配票买入 106：配票卖出 107：调账 108：出金手续费 109：出金手续费回退 110：出金回退 111：费用支付 112：费用补扣 113：手续费结算收入 114：采购专款结算收入
     */
    public Byte getTradeType() {
        return tradeType;
    }

    /**
     * 设置状态:101：入金 102:出金 103：本票买入 104：本票卖出 105：配票买入 106：配票卖出 107：调账 108：出金手续费 109：出金手续费回退 110：出金回退 111：费用支付 112：费用补扣 113：手续费结算收入 114：采购专款结算收入
     *
     * @param tradeType 状态:101：入金 102:出金 103：本票买入 104：本票卖出 105：配票买入 106：配票卖出 107：调账 108：出金手续费 109：出金手续费回退 110：出金回退 111：费用支付 112：费用补扣 113：手续费结算收入 114：采购专款结算收入
     */
    public void setTradeType(Byte tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * 获取变动后可提现金额
     *
     * @return withdraw - 变动后可提现金额
     */
    public BigDecimal getWithdraw() {
        return withdraw;
    }

    /**
     * 设置变动后可提现金额
     *
     * @param withdraw 变动后可提现金额
     */
    public void setWithdraw(BigDecimal withdraw) {
        this.withdraw = withdraw;
    }

    /**
     * 获取变动后余额
     *
     * @return balance - 变动后余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置变动后余额
     *
     * @param balance 变动后余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取变动数额
     *
     * @return change_amount - 变动数额
     */
    public String getChangeAmount() {
        return changeAmount;
    }

    /**
     * 设置变动数额
     *
     * @param changeAmount 变动数额
     */
    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    /**
     * 获取订单号
     *
     * @return order_no - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取收入或支出（1：支出2：收入）
     *
     * @return retype - 收入或支出（1：支出2：收入）
     */
    public Byte getRetype() {
        return retype;
    }

    /**
     * 设置收入或支出（1：支出2：收入）
     *
     * @param retype 收入或支出（1：支出2：收入）
     */
    public void setRetype(Byte retype) {
        this.retype = retype;
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
     * 获取状态（1：有效0：无效）
     *
     * @return flag - 状态（1：有效0：无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：有效0：无效）
     *
     * @param flag 状态（1：有效0：无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}