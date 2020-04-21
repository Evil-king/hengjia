package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/8/5 10:26
 * @description:
 */
@Data
public class ChangeCouponAccountDto extends ChangeAmountDto{
    @NotBlank(message = "商品交易编号不能为空")
    private String productTradeNo;
    //vouchers:兑换券 deliveryTicket:提货券
    @NotBlank(message = "券类型不能为空")
    private String couponType;
}
