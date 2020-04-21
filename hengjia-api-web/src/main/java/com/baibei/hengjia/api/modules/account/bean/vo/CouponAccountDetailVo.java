package com.baibei.hengjia.api.modules.account.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/10/10 16:29
 * @description:
 */
@Data
public class CouponAccountDetailVo {
    //券总额
    private BigDecimal totalCouponBalance;
    //可用券
    private BigDecimal balance;
    //冻结券额度
    private BigDecimal frozenBalance;
    //解冻券额度
    private BigDecimal thawBalance;
    //商品交易编号
    private String productTradeNo;
}
