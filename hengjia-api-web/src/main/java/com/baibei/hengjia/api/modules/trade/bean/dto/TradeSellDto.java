package com.baibei.hengjia.api.modules.trade.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/15 15:52
 * @description:
 */
@Data
public class TradeSellDto extends CustomerBaseDto {
    /**
     * 持有明细ID
     */
    @NotNull(message = "持有ID不能为空")
    private Long id;

    /**
     * 卖出类型，list=挂牌卖出，delist=摘牌卖出
     */
    @NotBlank(message = "类型不能为空")
    private String type;

    /**
     * 委托单单号，即时卖出时需要
     */
    private String entrustNo;
}