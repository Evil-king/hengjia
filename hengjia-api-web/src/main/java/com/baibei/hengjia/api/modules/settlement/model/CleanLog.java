package com.baibei.hengjia.api.modules.settlement.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_clean_log")
public class CleanLog {
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
     * 批量标识
     */
    @Column(name = "func_flag")
    private String funcFlag;

    /**
     * 批量文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 文件大小
     */
    @Column(name = "file_size")
    private String fileSize;

    /**
     * 资金汇总账号
     */
    @Column(name = "sup_acct_id")
    private String supAcctId;

    /**
     * 清收买卖货款扎差金额
     */
    @Column(name = "qs_zc_amount")
    private BigDecimal qsZcAmount;

    /**
     * 冻结总金额
     */
    @Column(name = "freeze_amount")
    private BigDecimal freezeAmount;

    /**
     * 解冻总金额
     */
    @Column(name = "unfreeze_amount")
    private BigDecimal unfreezeAmount;

    /**
     * 损益扎差数
     */
    @Column(name = "sy_zc_amount")
    private BigDecimal syZcAmount;

    /**
     * 文件密码
     */
    private String reserve;

    /**
     * 请求应答信息
     */
    private String resp;

    /**
     * 状态，wait=待请求，success=请求成功，fail=请求失败
     */
    private String status;

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
     * 获取批量标识
     *
     * @return func_flag - 批量标识
     */
    public String getFuncFlag() {
        return funcFlag;
    }

    /**
     * 设置批量标识
     *
     * @param funcFlag 批量标识
     */
    public void setFuncFlag(String funcFlag) {
        this.funcFlag = funcFlag;
    }

    /**
     * 获取批量文件名
     *
     * @return file_name - 批量文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置批量文件名
     *
     * @param fileName 批量文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件大小
     *
     * @return file_size - 文件大小
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * 设置文件大小
     *
     * @param fileSize 文件大小
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 获取资金汇总账号
     *
     * @return sup_acct_id - 资金汇总账号
     */
    public String getSupAcctId() {
        return supAcctId;
    }

    /**
     * 设置资金汇总账号
     *
     * @param supAcctId 资金汇总账号
     */
    public void setSupAcctId(String supAcctId) {
        this.supAcctId = supAcctId;
    }

    /**
     * 获取清收买卖货款扎差金额
     *
     * @return qs_zc_amount - 清收买卖货款扎差金额
     */
    public BigDecimal getQsZcAmount() {
        return qsZcAmount;
    }

    /**
     * 设置清收买卖货款扎差金额
     *
     * @param qsZcAmount 清收买卖货款扎差金额
     */
    public void setQsZcAmount(BigDecimal qsZcAmount) {
        this.qsZcAmount = qsZcAmount;
    }

    /**
     * 获取冻结总金额
     *
     * @return freeze_amount - 冻结总金额
     */
    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    /**
     * 设置冻结总金额
     *
     * @param freezeAmount 冻结总金额
     */
    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    /**
     * 获取解冻总金额
     *
     * @return unfreeze_amount - 解冻总金额
     */
    public BigDecimal getUnfreezeAmount() {
        return unfreezeAmount;
    }

    /**
     * 设置解冻总金额
     *
     * @param unfreezeAmount 解冻总金额
     */
    public void setUnfreezeAmount(BigDecimal unfreezeAmount) {
        this.unfreezeAmount = unfreezeAmount;
    }

    /**
     * 获取损益扎差数
     *
     * @return sy_zc_amount - 损益扎差数
     */
    public BigDecimal getSyZcAmount() {
        return syZcAmount;
    }

    /**
     * 设置损益扎差数
     *
     * @param syZcAmount 损益扎差数
     */
    public void setSyZcAmount(BigDecimal syZcAmount) {
        this.syZcAmount = syZcAmount;
    }

    /**
     * 获取文件密码
     *
     * @return reserve - 文件密码
     */
    public String getReserve() {
        return reserve;
    }

    /**
     * 设置文件密码
     *
     * @param reserve 文件密码
     */
    public void setReserve(String reserve) {
        this.reserve = reserve;
    }

    /**
     * 获取请求应答信息
     *
     * @return resp - 请求应答信息
     */
    public String getResp() {
        return resp;
    }

    /**
     * 设置请求应答信息
     *
     * @param resp 请求应答信息
     */
    public void setResp(String resp) {
        this.resp = resp;
    }

    /**
     * 获取状态，wait=待请求，success=请求成功，fail=请求失败
     *
     * @return status - 状态，wait=待请求，success=请求成功，fail=请求失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态，wait=待请求，success=请求成功，fail=请求失败
     *
     * @param status 状态，wait=待请求，success=请求成功，fail=请求失败
     */
    public void setStatus(String status) {
        this.status = status;
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