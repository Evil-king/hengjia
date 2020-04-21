package com.baibei.hengjia.api.modules.match.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Classname ExchangeDto
 * @Description 兑换dto
 * @Date 2019/8/5 15:54
 * @Created by Longer
 */
@Data
public class ExchangeDto {
    /**
     * 券账户id
     */
    @NotNull(message = "未指定券账户id")
    private Long couponAccountId;

    /**
     * 兑换数量
     */
    @NotNull(message = "未指定兑换数量")
    private Integer exchangeNum;
}
