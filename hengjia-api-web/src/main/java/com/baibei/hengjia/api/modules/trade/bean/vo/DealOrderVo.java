package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/6/6 13:52
 * @description:
 */
@Data
public class DealOrderVo {
    /**
     * 成交单号
     */
    private String dealNo;
    /**
     * 交易类型 4种类型：本票买入，配票买入（实为赠送），本票卖出和配票卖出。包括本票兑换，以及本票兑付
     */
    private String direction;
    /**
     * 支出或收入 （buy：收入 sell:支出）
     */
    private String retype;
    /**
     * 成交时间
     */
    private Date createTime;
    /**
     * 商品名称
     */
    private String productTradeName;
    /**
     * 商品编号
     */
    private String productTradeNo;
    /**
     * 加个
     */
    private BigDecimal price;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 总价
     */
    private BigDecimal totalPrice;
    /**
     * 商品图片名称
     */
    private String imageUrl;

    private String resource;
}
