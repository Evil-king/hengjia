package com.baibei.hengjia.api.modules.product.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/15 15:33
 * @description:
 */
@Data
public class ProductMarketNewDto {
    @NotNull(message = "持有ID不能为空")
    private Long id;
}