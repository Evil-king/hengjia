package com.baibei.hengjia.api.modules.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_deal_hold_ref")
public class DealHoldRef {
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
     * 关联的持仓详情ID
     */
    @Column(name = "hold_details_id")
    private Long holdDetailsId;

    /**
     * 扣除数量
     */
    @Column(name = "deduct_count")
    private BigDecimal deductCount;

    public BigDecimal getDeductCount() {
        return deductCount;
    }

    public void setDeductCount(BigDecimal deductCount) {
        this.deductCount = deductCount;
    }

    /**
     * 成交时间
     */
    @Column(name = "create_time")
    private Date createTime;

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
     * 获取关联的持仓详情ID
     *
     * @return hold_details_id - 关联的持仓详情ID
     */
    public Long getHoldDetailsId() {
        return holdDetailsId;
    }

    /**
     * 设置关联的持仓详情ID
     *
     * @param holdDetailsId 关联的持仓详情ID
     */
    public void setHoldDetailsId(Long holdDetailsId) {
        this.holdDetailsId = holdDetailsId;
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