package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/10/10 16:03
 * @description:
 */
@Data
public class ThawCouponAccountDto extends CustomerNoDto{
    //（vouchers:兑换券 deliveryTicket:提货券）
    @NotNull(message = "券类型不能为空")
    private String couponType;
    @NotNull(message = "解冻金额不能为空")
    private BigDecimal thawAmount;
    @NotBlank(message = "商品交易编号不能为空")
    private String productTradeNo;
}
