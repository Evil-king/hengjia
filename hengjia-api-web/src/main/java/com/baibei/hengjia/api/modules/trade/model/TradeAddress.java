package com.baibei.hengjia.api.modules.trade.model;

import org.springframework.validation.annotation.Validated;

import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Table(name = "tbl_tra_address")
public class TradeAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户编号
     */
    @Column(name = "customer_no")
    @NotBlank(message = "用户未指定")
    private String customerNo;

    /**
     * 收货人姓名
     */
    @Column(name = "customer_name")
    @NotBlank(message = "姓名不能为空")
    private String customerName;

    /**
     * 收货人手机号码
     */
    @NotNull(message = "手机号码不能为空")
    private Long mobile;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String address;

    /**
     * 邮政编码
     */
    @Column(name = "postal_code")
    private String postalCode;

    /**
     * 是否默认（1：默认；0：非默认）
     */
    private Integer defaultflag;

    private Date createtime;

    private Date updatetime;

    /**
     * 是否启用（1：启用；0：禁用）
     */
    private Integer flag=1;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
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
     * 获取收货人姓名
     *
     * @return customer_name - 收货人姓名
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置收货人姓名
     *
     * @param customerName 收货人姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取收货人手机号码
     *
     * @return mobile - 收货人手机号码
     */
    public Long getMobile() {
        return mobile;
    }

    /**
     * 设置收货人手机号码
     *
     * @param mobile 收货人手机号码
     */
    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取收货地址
     *
     * @return address - 收货地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置收货地址
     *
     * @param address 收货地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取邮政编码
     *
     * @return postal_code - 邮政编码
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 设置邮政编码
     *
     * @param postalCode 邮政编码
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * 获取是否默认（1：默认；0：非默认）
     *
     * @return defaultflag - 是否默认（1：默认；0：非默认）
     */
    public Integer getDefaultflag() {
        return defaultflag;
    }

    /**
     * 设置是否默认（1：默认；0：非默认）
     *
     * @param defaultflag 是否默认（1：默认；0：非默认）
     */
    public void setDefaultflag(Integer defaultflag) {
        this.defaultflag = defaultflag;
    }

    /**
     * @return createtime
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * @param createtime
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * @return updatetime
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * @param updatetime
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * 获取是否启用（1：启用；0：禁用）
     *
     * @return flag - 是否启用（1：启用；0：禁用）
     */
    public Integer getFlag() {
        return flag;
    }

    /**
     * 设置是否启用（1：启用；0：禁用）
     *
     * @param flag 是否启用（1：启用；0：禁用）
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}