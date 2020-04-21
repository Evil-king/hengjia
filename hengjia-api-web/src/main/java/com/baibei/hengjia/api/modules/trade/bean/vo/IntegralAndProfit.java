package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/8/15 10:43
 * @description:
 */
@Data
public class IntegralAndProfit {
    private BigDecimal integral = BigDecimal.ZERO;
    private BigDecimal profit = BigDecimal.ZERO;
    private BigDecimal couponAmount = BigDecimal.ZERO;
    private BigDecimal hongmuFund = BigDecimal.ZERO;
}