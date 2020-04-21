package com.baibei.hengjia.api.modules.user.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_customer_address")
public class CustomerAddress {
    /**
     * 主键ID
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
     * 区
     */
    private String area;

    /**
     * 市
     */
    private String city;

    /**
     * 省
     */
    private String province;

    /**
     * 收货详细地址
     */
    @Column(name = "receiving_address")
    private String receivingAddress;

    /**
     * 收货人姓名
     */
    @Column(name = "receiving_name")
    private String receivingName;

    /**
     * 收货手机号
     */
    @Column(name = "receiving_mobile")
    private String receivingMobile;

    /**
     * 是否默认(1:默认，0：非默认)
     */
    @Column(name = "default_address")
    private Boolean defaultAddress;

    /**
     * 邮政编码
     */
    @Column(name = "post_code")
    private String postCode;

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
     * 状态（1：正常；0：已删除）
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
     * 获取区
     *
     * @return area - 区
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区
     *
     * @param area 区
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取市
     *
     * @return city - 市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置市
     *
     * @param city 市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取省
     *
     * @return province - 省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省
     *
     * @param province 省
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取收货详细地址
     *
     * @return receiving_address - 收货详细地址
     */
    public String getReceivingAddress() {
        return receivingAddress;
    }

    /**
     * 设置收货详细地址
     *
     * @param receivingAddress 收货详细地址
     */
    public void setReceivingAddress(String receivingAddress) {
        this.receivingAddress = receivingAddress;
    }

    /**
     * 获取收货人姓名
     *
     * @return receiving_name - 收货人姓名
     */
    public String getReceivingName() {
        return receivingName;
    }

    /**
     * 设置收货人姓名
     *
     * @param receivingName 收货人姓名
     */
    public void setReceivingName(String receivingName) {
        this.receivingName = receivingName;
    }

    /**
     * 获取收货手机号
     *
     * @return receiving_mobile - 收货手机号
     */
    public String getReceivingMobile() {
        return receivingMobile;
    }

    /**
     * 设置收货手机号
     *
     * @param receivingMobile 收货手机号
     */
    public void setReceivingMobile(String receivingMobile) {
        this.receivingMobile = receivingMobile;
    }

    /**
     * 获取是否默认(1:默认，0：非默认)
     *
     * @return default_address - 是否默认(1:默认，0：非默认)
     */
    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    /**
     * 设置是否默认(1:默认，0：非默认)
     *
     * @param defaultAddress 是否默认(1:默认，0：非默认)
     */
    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    /**
     * 获取邮政编码
     *
     * @return post_code - 邮政编码
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * 设置邮政编码
     *
     * @param postCode 邮政编码
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
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
     * 获取状态（1：正常；0：已删除）
     *
     * @return flag - 状态（1：正常；0：已删除）
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态（1：正常；0：已删除）
     *
     * @param flag 状态（1：正常；0：已删除）
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }


}