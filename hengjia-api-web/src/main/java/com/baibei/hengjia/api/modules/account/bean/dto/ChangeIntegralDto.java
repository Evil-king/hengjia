package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

/**
 * @author: hyc
 * @date: 2019/6/4 11:03
 * @description:
 */
@Data
public class ChangeIntegralDto extends  ChangeAmountDto{
    /**
     * 积分类型ID,暂时只有商品积分类型：101
     */
    @NotNull(message = "积分类型不能为空")
    private Long integralNo;
}
