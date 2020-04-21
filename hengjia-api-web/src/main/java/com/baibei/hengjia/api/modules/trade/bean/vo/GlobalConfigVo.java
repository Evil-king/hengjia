package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/15 4:00 PM
 * @description:
 */
@Data
public class GlobalConfigVo {
    /**
     * 出金手续费
     */
    private BigDecimal withdrawFeeRate;

    /**
     * 最小提货量
     */
    private Integer minDeliveryCount;

    /**
     * 最小出金手续费
     */
    private Integer withdrawFee;

    /**
     * 微信渠道(0:关 1:开)
     */
    private Integer weixinChannel;

    /**
     * 平安出金(0:关 1:开)
     */
    private Integer pinganChannel;

    /**
     * 支付宝开关(0:关 1:开)
     */
    private Integer aliPayChannel;


    /**
     * 签约个人还是企业 true为个人,false 企业,
     */
    private Boolean  personalOrEnterprise;

}
