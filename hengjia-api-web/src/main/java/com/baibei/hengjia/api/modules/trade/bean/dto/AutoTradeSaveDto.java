package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/23 13:50
 * @description:
 */
@Data
public class AutoTradeSaveDto extends CustomerBaseDto {
    // 商品编码
    @NotBlank(message = "交易商品编码不能为空")
    private String productTradeNo;
    // 自动买入价格
    @NotNull(message = "自动买入价格不能为空")
    private BigDecimal autoBuyPrice;
    // 自动买入数量
    @NotNull(message = "自动买入数量不能为空")
    private Integer autoBuyCount;
    // 自动卖出价格
    @NotNull(message = "自动卖出价格不能为空")
    private BigDecimal autoSellPrice;
    // 自动卖出数量
    @NotNull(message = "自动卖出数量不能为空")
    private Integer autoSellCount;
}