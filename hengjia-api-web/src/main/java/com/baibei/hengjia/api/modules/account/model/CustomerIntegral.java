package com.baibei.hengjia.api.modules.account.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_integral")
public class CustomerIntegral {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 积分余额
     */
    private BigDecimal balance;

    /**
     * 积分类型ID
     */
    @Column(name = "integral_no")
    private Long integralNo;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

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
     * 删除状态（1：有效，0:无效）
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
     * 获取积分余额
     *
     * @return balance - 积分余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置积分余额
     *
     * @param balance 积分余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取积分类型ID
     *
     * @return integral_no - 积分类型ID
     */
    public Long getIntegralNo() {
        return integralNo;
    }

    /**
     * 设置积分类型ID
     *
     * @param integralNo 积分类型ID
     */
    public void setIntegralNo(Long integralNo) {
        this.integralNo = integralNo;
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
     * 获取删除状态（1：有效，0:无效）
     *
     * @return flag - 删除状态（1：有效，0:无效）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置删除状态（1：有效，0:无效）
     *
     * @param flag 删除状态（1：有效，0:无效）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}