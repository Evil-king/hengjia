package com.baibei.hengjia.api.modules.cash.bean.vo;

import com.baibei.hengjia.api.modules.cash.base.BaseResponse;
import lombok.Data;

import java.util.Date;

/**
 * 平安交易网查询银行清算与对账文件的进度返回参数
 */
@Data
public class FilePlannedSpeedVo extends BaseResponse {

    /**
     * 批量次数
     */
    private Integer recordCount;

    /**
     * 处理结果标识
     */
    private String resultFlag;

}
