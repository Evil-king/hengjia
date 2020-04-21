package com.baibei.hengjia.api.modules.user.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/6/20 10:01
 * @description:
 */
@Data
public class StatisticalCustomerVo  {
    /**
     * 总提现
     */
    private BigDecimal totalWithdraw;
    /**
     * 当日提现金额
     */
    private BigDecimal todayWithdraw;
    /**
     * 总注册人数
     */
    private Integer totalRegist;
    /**
     * 当日注册人数
     */
    private Integer todayRegist;
    /**
     * 总充值
     */
    private BigDecimal totalRecharge;
    /**
     * 当日充值数额
     */
    private BigDecimal todayRecharge;
    /**
     * 未充值人数
     */
    private Integer withoutRecharge;
    /**
     * 充值成功人数
     */
    private Integer rechargeAmount;
    /**
     * 总成交手数
     */
    private Integer totalDealCount;
    /**
     * 有效人数(个人账户中，总资产（余额+持仓等值金额）大于等于2000的人)
     */
    private Integer effectiveAmount;
}
