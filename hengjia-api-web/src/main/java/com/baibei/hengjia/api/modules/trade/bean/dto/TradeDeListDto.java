package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 11:42 AM
 * @description:
 */
@Data
public class TradeDeListDto extends CustomerBaseDto {
    // 商品交易商编码
    @NotBlank(message = "委托单单号不能为空")
    private String entrustNo;

    // 数量
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    private Integer count;

    // 方向,BUY=买入,SELL=卖出
    @NotBlank(message = "方向不能为空")
    private String direction;

    // 是否超过一百手
    private Integer oneHundred;

    // 来源，auto_trade=自动交易，normal=正常交易
    private String resource;

    /**
     * 卖出持仓明细ID
     */
    private Long id;
}
