package com.baibei.hengjia.admin.modules.settlement.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_set_clean_flow_path")
public class CleanFlowPath {
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
     * 顺序
     */
    private Integer sequence;

    /**
     * 执行内容编码，sign_in=签到，sign_back=签退，
  accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
  clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度，clean_result=获取清算差异结果
     */
    @Column(name = "project_code")
    private String projectCode;

    /**
     * 执行内容名称
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 状态，wait=待处理，completed=已处理，doing=处理中
     */
    private String status;

    /**
     * 创执行时间
     */
    @Column(name = "execute_time")
    private Date executeTime;

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
     * 获取顺序
     *
     * @return sequence - 顺序
     */
    public Integer getSequence() {
        return sequence;
    }

    /**
     * 设置顺序
     *
     * @param sequence 顺序
     */
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    /**
     * 获取执行内容编码，sign_in=签到，sign_back=签退，
  accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
  clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度，clean_result=获取清算差异结果
     *
     * @return project_code - 执行内容编码，sign_in=签到，sign_back=签退，
  accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
  clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度，clean_result=获取清算差异结果
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * 设置执行内容编码，sign_in=签到，sign_back=签退，
  accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
  clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度，clean_result=获取清算差异结果
     *
     * @param projectCode 执行内容编码，sign_in=签到，sign_back=签退，
  accountcheck=发起出入金对账，accountcheck_file=获取出入金对账文件，amount_return=生成业务返还数据，
  clean_file=生成清算文件，launch_clean=发起清算，clean_process=查看清算进度，clean_result=获取清算差异结果
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * 获取执行内容名称
     *
     * @return project_name - 执行内容名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * 设置执行内容名称
     *
     * @param projectName 执行内容名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 获取状态，wait=待处理，completed=已处理，doing=处理中
     *
     * @return status - 状态，wait=待处理，completed=已处理，doing=处理中
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态，wait=待处理，completed=已处理，doing=处理中
     *
     * @param status 状态，wait=待处理，completed=已处理，doing=处理中
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取创执行时间
     *
     * @return execute_time - 创执行时间
     */
    public Date getExecuteTime() {
        return executeTime;
    }

    /**
     * 设置创执行时间
     *
     * @param executeTime 创执行时间
     */
    public void setExecuteTime(Date executeTime) {
        this.executeTime = executeTime;
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