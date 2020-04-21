package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Longer
 * @date: 2019/6/16 16:47 PM
 * @description:
 */
@Data
public class MyDeliveryHoldVo {
    /**
     * 交易商编码
     */
    private String customerNo;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 商品总量
     */
    private BigDecimal totalCount;

    /**
     * 冻结数量
     */
    private BigDecimal frozenCount;

    /**
     * 可提数量（总量-冻结数量）
     */
    private BigDecimal canDeliveryCount;

    /**
     * 可卖商品数量
     */
    private BigDecimal canSellCount;

    /**
     * 持仓单类型，main=本票，match=配票
     */
    private String holdType;

    private BigDecimal newPrice;  // 最新价

    private String productImg;  // 商品图片地址

    private String productName;

    /**
     * 来源 exchange=兑换
     */
    private String resource;
}
