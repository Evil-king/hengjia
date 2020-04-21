package com.baibei.hengjia.api.modules.cash.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_cash_sign_in_back")
public class SignInBack {
    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 签到状态(1:签到2:签退)
     */
    @Column(name = "sign_status")
    private String signStatus;

    /**
     * 签约流水号
     */
    @Column(name = "sign_no")
    private String signNo;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 是否删除(0:删除,1:未删除)
     */
    private Byte flag;

    /**
     * 交易时间
     */
    @Column(name = "tx_date")
    private Date txDate;

    /**
     * 保留域
     */
    private String reserve;

    @Column(name = "external_no")
    private String externalNo;

    /**
     * 签到|签退状态
     */
    @Column(name = "status")
    private String status;

    /**
     * 获取自增主键
     *
     * @return id - 自增主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增主键
     *
     * @param id 自增主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取签到状态(1:签到2:签退)
     *
     * @return sign_status - 签到状态(1:签到2:签退)
     */
    public String getSignStatus() {
        return signStatus;
    }

    /**
     * 设置签到状态(1:签到2:签退)
     *
     * @param signStatus 签到状态(1:签到2:签退)
     */
    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    /**
     * 获取签约流水号
     *
     * @return sign_no - 签约流水号
     */
    public String getSignNo() {
        return signNo;
    }

    /**
     * 设置签约流水号
     *
     * @param signNo 签约流水号
     */
    public void setSignNo(String signNo) {
        this.signNo = signNo;
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
     * 获取是否删除(0:删除,1:未删除)
     *
     * @return flag - 是否删除(0:删除,1:未删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(0:删除,1:未删除)
     *
     * @param flag 是否删除(0:删除,1:未删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    /**
     * 获取交易时间
     *
     * @return tx_date - 交易时间
     */
    public Date getTxDate() {
        return txDate;
    }

    /**
     * 设置交易时间
     *
     * @param txDate 交易时间
     */
    public void setTxDate(Date txDate) {
        this.txDate = txDate;
    }

    /**
     * 获取保留域
     *
     * @return reserve - 保留域
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * 设置保留域
     *
     * @param reserve 保留域
     */
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    public String getExternalNo() {
        return externalNo;
    }

    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}