package com.baibei.hengjia.api.modules.settlement.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Classname DealDiffDto
 * @Description 出入金对账处理dto
 * @Date 2019/6/27 14:16
 * @Created by Longer
 */
@Data
public class DiffDto {
    /**
     * 批次
     */
    @NotNull(message = "未指定批次")
    private String batchNo;
}
