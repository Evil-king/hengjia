package com.baibei.hengjia.api.modules.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_clean_help")
public class CleanHelp {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 盈利方客户编号
     */
    @Column(name = "profit_customer_no")
    private String profitCustomerNo;

    /**
     * 亏损方的客户编号
     */
    @Column(name = "loss_customer_no")
    private String lossCustomerNo;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 类型，transfer=转账类型
     */
    @Column(name = "help_type")
    private String helpType;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private String businessType;

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
     * 状态(1:正常，0:删除)
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
     * 获取盈利方客户编号
     *
     * @return profit_customer_no - 盈利方客户编号
     */
    public String getProfitCustomerNo() {
        return profitCustomerNo;
    }

    /**
     * 设置盈利方客户编号
     *
     * @param profitCustomerNo 盈利方客户编号
     */
    public void setProfitCustomerNo(String profitCustomerNo) {
        this.profitCustomerNo = profitCustomerNo;
    }

    /**
     * 获取亏损方的客户编号
     *
     * @return loss_customer_no - 亏损方的客户编号
     */
    public String getLossCustomerNo() {
        return lossCustomerNo;
    }

    /**
     * 设置亏损方的客户编号
     *
     * @param lossCustomerNo 亏损方的客户编号
     */
    public void setLossCustomerNo(String lossCustomerNo) {
        this.lossCustomerNo = lossCustomerNo;
    }

    /**
     * 获取退款金额
     *
     * @return amount - 退款金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置退款金额
     *
     * @param amount 退款金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取类型，transfer=转账类型
     *
     * @return help_type - 类型，transfer=转账类型
     */
    public String getHelpType() {
        return helpType;
    }

    /**
     * 设置类型，transfer=转账类型
     *
     * @param helpType 类型，transfer=转账类型
     */
    public void setHelpType(String helpType) {
        this.helpType = helpType;
    }

    /**
     * 获取业务类型
     *
     * @return business_type - 业务类型
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型
     *
     * @param businessType 业务类型
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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
     * 获取状态(1:正常，0:删除)
     *
     * @return flag - 状态(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:删除)
     *
     * @param flag 状态(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}