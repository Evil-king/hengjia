package com.baibei.hengjia.admin.modules.settlement.bean.vo;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class CleanFlowPathVo {

    private Long id;

    /**
     * 批次号
     */
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
    private String projectCode;

    /**
     * 执行内容名称
     */
    private String projectName;

    /**
     * 状态，wait=待处理，completed=已处理，doing=处理中
     */
    private String status;

    /**
     * 创执行时间
     */
    private Date executeTime;
}
