package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:19 PM
 * @description:
 */
@Data
public class EntrustOrderVo {
    private Integer entrustId;
    private String entrustNo;
    private String productTradeNo;
    private String productName;
    private BigDecimal price;
    private Integer entrustCount;
    private String customerNo;
    private String customerName;
    private Integer minTrade;
    private String direction;
    private Integer dealCount;
}
