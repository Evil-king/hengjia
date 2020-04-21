package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:47 PM
 * @description:
 */
@Data
public class MyHoldVo {
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
    private Integer totalCount;

    /**
     * 可卖商品数量
     */
    private Integer canSellCount;

    /**
     * 持仓单类型，main=本票，match=配票
     */
    private String holdType;

    private BigDecimal newPrice;  // 最新价

    private String productImg;  // 商品图片地址

    private String productName;

    // 是否能提货标识
    private Boolean deliveryFlag;

    // 最小提货数量
    private Integer deliveryNum;
}
