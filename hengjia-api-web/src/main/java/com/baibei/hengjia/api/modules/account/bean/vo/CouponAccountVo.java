package com.baibei.hengjia.api.modules.account.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname CouponAccountVo
 * @Description TODO
 * @Date 2019/8/5 14:59
 * @Created by Longer
 */
@Data
public class CouponAccountVo {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 用户编码
     */
    private String customerNo;
    /**
     * 商品交易编码
     */
    private String productTradeNo;
    /**
     * 商品交易名称
     */
    private String productTradeName;
    /**
     * 零售价
     */
    private BigDecimal issuePrice;

    /**
     * 券余额
     */
    private BigDecimal balance;

    /**
     * 可兑数量
     */
    private int exchangenNum;

    /**
     * 类型
     */
    private String couponType;
}
