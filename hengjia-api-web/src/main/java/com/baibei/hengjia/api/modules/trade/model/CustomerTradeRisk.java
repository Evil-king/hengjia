package com.baibei.hengjia.api.modules.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_customer_trade_risk")
public class CustomerTradeRisk {
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
     * 客户姓名
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 交易参数
     */
    @Column(name = "trade_param")
    private String tradeParam;

    /**
     * 生效状态，valid=生效；unvalid=失效
     */
    private String status;

    /**
     * 注册时间（冗余）
     */
    @Column(name = "register_time")
    private Date registerTime;

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
     * 获取客户姓名
     *
     * @return customer_name - 客户姓名
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户姓名
     *
     * @param customerName 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取交易参数
     *
     * @return trade_param - 交易参数
     */
    public String getTradeParam() {
        return tradeParam;
    }

    /**
     * 设置交易参数
     *
     * @param tradeParam 交易参数
     */
    public void setTradeParam(String tradeParam) {
        this.tradeParam = tradeParam;
    }

    /**
     * 获取生效状态，valid=生效；unvalid=失效
     *
     * @return status - 生效状态，valid=生效；unvalid=失效
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置生效状态，valid=生效；unvalid=失效
     *
     * @param status 生效状态，valid=生效；unvalid=失效
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取注册时间（冗余）
     *
     * @return register_time - 注册时间（冗余）
     */
    public Date getRegisterTime() {
        return registerTime;
    }

    /**
     * 设置注册时间（冗余）
     *
     * @param registerTime 注册时间（冗余）
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
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