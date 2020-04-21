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
public class DealDiffDto {
    /**
     * 差异ID
     */
    @NotNull(message = "未指定差异流水")
    private Long diffId;
}
