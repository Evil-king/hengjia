package com.baibei.hengjia.api.modules.settlement.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname WithDrawDepositDto
 * @Description 出入金对账
 * @Date 2019/6/27 14:16
 * @Created by Longer
 */
@Data
public class WithDrawDepositDto {
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 外部订单号
     */
    private String externalNo;
    /**
     * withdraw=出金，deposit=入金
     */
    private String type;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 状态(出金状态和入金状态)
     */
    private String status;

}
