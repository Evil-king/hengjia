package com.baibei.hengjia.api.modules.product.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/6/5 18:59
 * @description:
 */
@Data
public class ProductMarketVo {
    /**
     * 商品名称
     */
    private String productTradeName;
    /**
     * 发行价
     */
    private BigDecimal issuePrice;
    /**
     * 默认买入/卖出 数量
     */
    private Integer defaultAmount;
    /**
     *可卖/可买 数量
     */
    private Integer productAmount;

    /**
     * 挂牌买入一档数量
     */
    private Integer buyAmount;
    /**
     * 挂牌卖出一档数量
     *
     */
    private Integer sellAmount;
    /**
     * 挂牌买入一档价格
     */
    private BigDecimal buyPrice;
    /**
     * 挂牌卖出一档价格
     *
     */
    private BigDecimal sellPrice;
}
