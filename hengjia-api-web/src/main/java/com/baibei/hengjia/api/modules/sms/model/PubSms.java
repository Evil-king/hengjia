package com.baibei.hengjia.api.modules.sms.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pub_sms")
public class PubSms {
    /**
     * 主键
     */
    @Id
    @Column(name = "sms_id")
    private Long smsId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;

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
     * 状态(1:正常，0:已删除)
     */
    private Byte flag;

    /**
     * 获取主键
     *
     * @return sms_id - 主键
     */
    public Long getSmsId() {
        return smsId;
    }

    /**
     * 设置主键
     *
     * @param smsId 主键
     */
    public void setSmsId(Long smsId) {
        this.smsId = smsId;
    }

    /**
     * 获取手机号码
     *
     * @return mobile - 手机号码
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     *
     * @param mobile 手机号码
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取验证码
     *
     * @return code - 验证码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置验证码
     *
     * @param code 验证码
     */
    public void setCode(String code) {
        this.code = code;
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
     * 获取状态(1:正常，0:已删除)
     *
     * @return flag - 状态(1:正常，0:已删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:已删除)
     *
     * @param flag 状态(1:正常，0:已删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}