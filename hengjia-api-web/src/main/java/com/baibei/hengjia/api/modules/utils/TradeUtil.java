package com.baibei.hengjia.api.modules.utils;

import com.baibei.hengjia.common.tool.constants.Constants;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/16 14:14
 * @description:
 */
public class TradeUtil {

    /**
     * 获取卖出持仓明细的仓单类型
     *
     * @param holdType
     * @param resource
     * @return
     */
    public static String getSellType(String holdType, String resource) {
        if (Constants.HoldType.MAIN.equals(holdType)) {
            return Constants.SellHoldType.MAIN;
        }
        if (Constants.HoldType.MATCH.equals(holdType) && "exchange".equals(resource)) {
            return Constants.SellHoldType.EXCHANGE;
        } else {
            return Constants.SellHoldType.MATCH;
        }
    }
}