package com.baibei.hengjia.api.modules.account.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/28 19:43
 * @description:
 */
@Data
public class AccountVo {
    private String customerNo;
    //余额
    private BigDecimal balance;
    //冻结资金（挂牌买入冻结）
    private BigDecimal freezingAmount;
    //可提现金额
    private BigDecimal withdrawableCash;
    //分润冻结资金（只做展示，业务上无此项资金）
    private BigDecimal freezingBonus;
    //总资金
    private BigDecimal totalBalance;
}
