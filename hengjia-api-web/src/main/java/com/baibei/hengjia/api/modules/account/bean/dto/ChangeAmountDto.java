package com.baibei.hengjia.api.modules.account.bean.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/5/29 10:31
 * @description:
 */
@Data
@ToString
public class ChangeAmountDto implements Serializable {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;
    @NotNull(message = "变动资金不能为空")
    private BigDecimal changeAmount;
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    @NotNull(message = "交易类型不能为空")
    private byte tradeType;
    //类型：1：解冻(支出)，2：冻结（收入）
    @NotNull(message = "类型不能为空")
    private byte reType;
}
