package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: hyc
 * @date: 2019/10/10 16:32
 * @description:
 */
@Data
public class CouponAccountDetailDto extends CustomerNoDto{
    //vouchers:兑换券 deliveryTicket:提货券
    @NotBlank(message = "券类型不能为空")
    private String couponType;
}
