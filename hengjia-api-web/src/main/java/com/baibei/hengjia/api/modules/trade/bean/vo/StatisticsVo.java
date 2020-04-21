package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/30 10:58 AM
 * @description:
 */
@Data
public class StatisticsVo {
    // 总持仓市值
    private BigDecimal marketValue;

    // 总持仓盈亏
    private BigDecimal profitAndLoss;

}
