package com.baibei.hengjia.api.modules.trade.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Classname DeliveryTransferDto
 * @Description 用户提货dto（提货申请）
 * @Date 2019/10/08 17:59
 * @Created by Longer
 */
@Data
public class DeliveryTransferDto{

    @NotBlank(message = "客户编码不能为空")
    private String customerNo;
    /*
        商品交易编码
     */
    @NotBlank(message = "未指定提货商品")
    private String productTradeNo;


}
