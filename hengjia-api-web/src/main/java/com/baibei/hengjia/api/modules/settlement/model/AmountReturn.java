package com.baibei.hengjia.api.modules.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_amount_return")
public class AmountReturn {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次号
     */
    @Column(name = "batch_no")
    private String batchNo;

    /**
     * 用户账号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 待返比例
     */
    @Column(name = "return_rate")
    private BigDecimal returnRate;

    /**
     * 待返金额
     */
    @Column(name = "return_amount")
    private BigDecimal returnAmount;

    /**
     * 状态，wait=待返，completed=已返
     */
    private String status;

    /**
     * 返还类型，fee=手续费返还办理，integral=积分返还办理
     */
    private String type;

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
     * 获取批次号
     *
     * @return batch_no - 批次号
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * 设置批次号
     *
     * @param batchNo 批次号
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 获取用户账号
     *
     * @return customer_no - 用户账号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置用户账号
     *
     * @param customerNo 用户账号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取待返比例
     *
     * @return return_rate - 待返比例
     */
    public BigDecimal getReturnRate() {
        return returnRate;
    }

    /**
     * 设置待返比例
     *
     * @param returnRate 待返比例
     */
    public void setReturnRate(BigDecimal returnRate) {
        this.returnRate = returnRate;
    }

    /**
     * 获取待返金额
     *
     * @return return_amount - 待返金额
     */
    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    /**
     * 设置待返金额
     *
     * @param returnAmount 待返金额
     */
    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    /**
     * 获取状态，wait=待返，completed=已返
     *
     * @return status - 状态，wait=待返，completed=已返
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态，wait=待返，completed=已返
     *
     * @param status 状态，wait=待返，completed=已返
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取返还类型，fee=手续费返还办理，integral=积分返还办理
     *
     * @return type - 返还类型，fee=手续费返还办理，integral=积分返还办理
     */
    public String getType() {
        return type;
    }

    /**
     * 设置返还类型，fee=手续费返还办理，integral=积分返还办理
     *
     * @param type 返还类型，fee=手续费返还办理，integral=积分返还办理
     */
    public void setType(String type) {
        this.type = type;
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