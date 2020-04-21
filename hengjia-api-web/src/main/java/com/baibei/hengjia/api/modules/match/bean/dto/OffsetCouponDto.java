package com.baibei.hengjia.api.modules.match.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname OffsetCouponDto
 * @Description  零售商品兑换券余额抵扣折扣商品DTO
 * @Date 2019/8/21 10:36
 * @Created by Longer
 */
@Data
public class OffsetCouponDto {
    /**
     * 流水号
     */
    @NotBlank(message = "流水号不能为空")
    private String offsetNo;

    @NotBlank(message = "用户编码不能为空")
    private String customerNo;
    /**
     * 应扣减券余额数
     */
    @NotNull(message = "应扣减券余额数不能为空")
    private BigDecimal detuchCouponAcct;
    /**
     * 需补金额（钱）
     */
    @NotNull(message = "需补金额不能为空")
    private BigDecimal offsetAmount;

    /**
     * 抵扣数量
     */
    @NotNull(message = "抵扣数量不能为空")
    private Integer offsetCount;

    /**
     * 成本
     */
    @NotNull(message = "成本不能为空")
    private BigDecimal cost;

    /**
     * 买方手续费
     */
    @NotNull(message = "买方手续费不能为空")
    private BigDecimal buyFee;

    /**
     * 卖方手续费
     */
    @NotNull(message = "卖方手续费不能为空")
    private BigDecimal sellFee;

    /**
     * 解锁时间
     */
    @NotBlank(message = "解锁时间不能为空")
    private String tradeDay;

    @NotBlank(message = "交易商品编码不能为空")
    private String productTradeNo;
}
