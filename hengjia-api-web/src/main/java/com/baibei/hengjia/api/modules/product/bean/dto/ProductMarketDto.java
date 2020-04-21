package com.baibei.hengjia.api.modules.product.bean.dto;

import com.baibei.hengjia.api.modules.account.bean.dto.CustomerNoDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/6/5 19:03
 * @description:
 */
@Data
public class ProductMarketDto extends CustomerNoDto {
    // 方向,BUY=买入,SELL=卖出
    @NotBlank(message = "方向不能为空")
    private String direction;
    @NotBlank(message = "商品交易编码不能为空")
    private String productTradeNo;
}
