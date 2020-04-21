package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/25 14:43
 * @description:
 */
@Data
public class AutoTradeCountVo {
    private String customerNo;
    private String productTradeNo;
    private Integer count;
    private BigDecimal autoPrice;
    private Integer autoCount;
}