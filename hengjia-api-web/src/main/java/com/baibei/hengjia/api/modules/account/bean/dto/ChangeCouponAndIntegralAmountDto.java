package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ToString
public class ChangeCouponAndIntegralAmountDto implements Serializable {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;
    //积分入参
    private ChangeIntegralDto changeIntegralDto;
    //券入参
    private ChangeCouponAccountDto changeCouponAccountDto;
}
