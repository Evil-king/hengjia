package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/15 1:55 PM
 * @description:
 */
@Data
public class ProductTradeNoDto extends CustomerBaseDto {
    @NotBlank(message = "商品交易编码不能为空")
    private String productTradeNo;
    @NotBlank(message = "方向不能为空")
    private String direction;
}
