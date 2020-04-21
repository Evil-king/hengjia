package com.baibei.hengjia.api.modules.cash.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tbl_temp_withdraw")
public class TempWithdraw {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 出金账号
     */
    @Column(name = "withdraw_no")
    private String withdrawNo;

    /**
     * 出金金额(不包含手续费)
     */
    private BigDecimal amount;

    /**
     * 状态(1、提现申请 2、提现审核通过 3、提现成功 4、提现失败)
     */
    private String status;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 是否扣钱(0、已扣 1、未扣)
     */
    private Byte sign;

    /**
     * 状态(1:正常，0:禁用)
     */
    private Byte flag;

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
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户编号
     *
     * @return customer_no - 用户编号
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置用户编号
     *
     * @param customerNo 用户编号
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取出金账号
     *
     * @return withdraw_no - 出金账号
     */
    public String getWithdrawNo() {
        return withdrawNo;
    }

    /**
     * 设置出金账号
     *
     * @param withdrawNo 出金账号
     */
    public void setWithdrawNo(String withdrawNo) {
        this.withdrawNo = withdrawNo;
    }

    /**
     * 获取出金金额(不包含手续费)
     *
     * @return amount - 出金金额(不包含手续费)
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置出金金额(不包含手续费)
     *
     * @param amount 出金金额(不包含手续费)
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取状态(1、提现申请 2、提现审核通过 3、提现成功 4、提现失败)
     *
     * @return status - 状态(1、提现申请 2、提现审核通过 3、提现成功 4、提现失败)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态(1、提现申请 2、提现审核通过 3、提现成功 4、提现失败)
     *
     * @param status 状态(1、提现申请 2、提现审核通过 3、提现成功 4、提现失败)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取手续费
     *
     * @return fee - 手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置手续费
     *
     * @param fee 手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 获取是否扣钱(0、已扣 1、未扣)
     *
     * @return sign - 是否扣钱(0、已扣 1、未扣)
     */
    public Byte getSign() {
        return sign;
    }

    /**
     * 设置是否扣钱(0、已扣 1、未扣)
     *
     * @param sign 是否扣钱(0、已扣 1、未扣)
     */
    public void setSign(Byte sign) {
        this.sign = sign;
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
}