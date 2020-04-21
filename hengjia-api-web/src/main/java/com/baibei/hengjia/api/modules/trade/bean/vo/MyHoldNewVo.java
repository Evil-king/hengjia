package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/15 15:10
 * @description:
 */
@Data
public class MyHoldNewVo {
    /**
     * 持仓明细ID
     */
    private Long id;
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
    private Integer totalCount=0;

    /**
     * 可卖商品数量
     */
    private Integer canSellCount=0;

    private BigDecimal originalCount;

    /**
     * 持仓单类型，main=零售商品，match=折扣商品，exchange=兑换商品
     */
    private String holdType;

    private BigDecimal newPrice;  // 最新价

    private String productImg;  // 商品图片地址

    private String productName;

    // 是否能提货标识
    private Boolean deliveryFlag;

    // 最小提货数量
    private Integer deliveryNum;

    // 持有时间
    private Date createTime;

    private Integer frozenCount;

    private Integer remindCount;

    private Byte scanner;

}