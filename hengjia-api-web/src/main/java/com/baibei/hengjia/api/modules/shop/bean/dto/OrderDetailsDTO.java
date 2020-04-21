package com.baibei.hengjia.api.modules.shop.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hwq
 * @date 2019/06/04
 * <p>
 *     积分订单详情
 * </p>
 */
@Data
public class OrderDetailsDTO {

    @NotBlank(message = "商品编号不能为空")
    private String spuNo;

    @NotBlank(message = "商品零售价不能为空")
    private String sellPrice;

    @NotBlank(message = "商品数量不能为空")
    private String num;
}
