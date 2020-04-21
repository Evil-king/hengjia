package com.baibei.hengjia.api.modules.settlement.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_clean_progress")
public class CleanProgress {
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
     * 处理结果标识
     */
    @Column(name = "funcFlag")
    private String funcflag;

    /**
     * 批量次数
     */
    @Column(name = "record_count")
    private Integer recordCount;

    /**
     * 处理结果标识
     */
    @Column(name = "result_flag")
    private String resultFlag;

    /**
     * 保留域
     */
    private String reserve;

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
     * 获取处理结果标识
     *
     * @return funcFlag - 处理结果标识
     */
    public String getFuncflag() {
        return funcflag;
    }

    /**
     * 设置处理结果标识
     *
     * @param funcflag 处理结果标识
     */
    public void setFuncflag(String funcflag) {
        this.funcflag = funcflag;
    }

    /**
     * 获取批量次数
     *
     * @return record_count - 批量次数
     */
    public Integer getRecordCount() {
        return recordCount;
    }

    /**
     * 设置批量次数
     *
     * @param recordCount 批量次数
     */
    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * 获取处理结果标识
     *
     * @return result_flag - 处理结果标识
     */
    public String getResultFlag() {
        return resultFlag;
    }

    /**
     * 设置处理结果标识
     *
     * @param resultFlag 处理结果标识
     */
    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
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