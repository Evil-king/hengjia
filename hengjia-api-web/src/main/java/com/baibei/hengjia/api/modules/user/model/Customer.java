package com.baibei.hengjia.api.modules.user.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 会员编码
     */
    @Column(name = "member_no")
    private String memberNo;

    /**
     * 推荐人交易商编码
     */
    @Column(name = "recommender_id")
    private String recommenderId;

    /**
     * 1,：普通用户 2：会员
     */
    @Column(name = "customer_type")
    private Byte customerType;

    /**
     * 100:正常101：限制商城登录102：限制交易登录等等
     */
    @Column(name = "customer_status")
    private String customerStatus;

    /**
     * 加密时的盐值
     */
    private String salt;

    /**
     * 二维码链接地址
     */
    private String qrcode;

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
     * openid
     */
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }


    /**
     * 是否签约(1:签约,0:未签约)
     */
    private String signing;


    /**
     * 是否已扣过 0:没扣过、1:扣过
     */
    private String deduction;


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
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取手机号
     *
     * @return mobile - 手机号
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取用户名
     *
     * @return username - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取会员编码
     *
     * @return member_no - 会员编码
     */
    public String getMemberNo() {
        return memberNo;
    }

    /**
     * 设置会员编码
     *
     * @param memberNo 会员编码
     */
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * 获取推荐人交易商编码
     *
     * @return recommender_id - 推荐人交易商编码
     */
    public String getRecommenderId() {
        return recommenderId;
    }

    /**
     * 设置推荐人交易商编码
     *
     * @param recommenderId 推荐人交易商编码
     */
    public void setRecommenderId(String recommenderId) {
        this.recommenderId = recommenderId;
    }

    /**
     * 获取1,：普通用户 2：会员
     *
     * @return customer_type - 1,：普通用户 2：会员
     */
    public Byte getCustomerType() {
        return customerType;
    }

    /**
     * 设置1,：普通用户 2：会员
     *
     * @param customerType 1,：普通用户 2：会员
     */
    public void setCustomerType(Byte customerType) {
        this.customerType = customerType;
    }

    /**
     * 获取100:正常101：限制商城登录102：限制交易登录等等
     *
     * @return customer_status - 100:正常101：限制商城登录102：限制交易登录等等
     */
    public String getCustomerStatus() {
        return customerStatus;
    }

    /**
     * 设置100:正常101：限制商城登录102：限制交易登录等等
     *
     * @param customerStatus 100:正常101：限制商城登录102：限制交易登录等等
     */
    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    /**
     * 获取加密时的盐值
     *
     * @return salt - 加密时的盐值
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置加密时的盐值
     *
     * @param salt 加密时的盐值
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取二维码链接地址
     *
     * @return qrcode - 二维码链接地址
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * 设置二维码链接地址
     *
     * @param qrcode 二维码链接地址
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
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

    public String getSigning() {
        return signing;
    }

    public void setSigning(String signing) {
        this.signing = signing;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }
}