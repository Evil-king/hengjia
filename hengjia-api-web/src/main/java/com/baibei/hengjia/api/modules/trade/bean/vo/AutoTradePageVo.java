package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/23 11:13
 * @description:
 */
@Data
public class AutoTradePageVo {
    // 商品编码
    private String productTradeNo;
    // 商品名称
    private String productTradeName;
    // 自动买入价格
    private BigDecimal autoBuyPrice;
    // 自动买入数量
    private Integer autoBuyCount;
    // 自动卖出价格
    private BigDecimal autoSellPrice;
    // 自动卖出数量
    private Integer autoSellCount;
    // 截止日期
    private String closingTime;
    // 开启状态
    private String status;

    // 客户姓名
    private String userName;
    // 当前日期
    private String currentDate;

    // 说明
    private List<String> noticeList;

    private String saveNotice;

    private Integer defaultBuyCount;
}