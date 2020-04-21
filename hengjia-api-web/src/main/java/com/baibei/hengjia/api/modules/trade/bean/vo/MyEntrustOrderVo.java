package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/4 3:39 PM
 * @description:
 */
@Data
public class MyEntrustOrderVo {
    private Integer entrustId;

    /**
     * 委托单单号
     */
    private String entrustNo;

    /**
     * 交易商编码
     */
    private String customerNo;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 委托时间
     */
    private Date entrustTime;

    /**
     * 委托方向，main_buy=本票买入，main_sell=本票卖出，match_sell=配票卖出
     */
    private String direction;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 委托数量
     */
    private Integer entrustCount;

    /**
     * 委托结果，wait_deal=待成交，all_deal=全部成交，some_deal=部分成交，revoke=撤单
     */
    private String result;

    private String productName;

    private String productImg;

    private String holdType;

    /**
     * 成交数量
     */
    private String dealCount;
}
