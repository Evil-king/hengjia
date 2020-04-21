package com.baibei.hengjia.api.modules.match.bean.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/8/8 9:19
 * @description:
 */
@Data
public class MatchPlanDetailDto {
    @NotBlank(message = "用户编号不能为空")
    private String customerNo;
    @NotBlank(message = "商品交易编号不能为空")
    private String productTradeNo;

    /**
     * 券类型
     */
    @NotBlank(message = "券类型不能为空")
    private String type;
    /**
     * 流水号
     */
    @NotBlank(message = "流水号不能为空")
    private String planNo;

    /**
     * 金额
     */
    @NotBlank(message = "金额不能为空")
    private BigDecimal price;
    @NotBlank(message = "冻结金额不能为空")
    private BigDecimal frozenAmount;
    @NotBlank(message = "解冻金额不能为空")
    private BigDecimal thawAmount;

    /**
     * 来源（ASSEMBLE_VOUCHERS:拼团代金券 GROUP_VOUCHERS：组团代金券）
     */
    @NotBlank(message = "来源不能为空")
    private String source;
    @NotBlank(message = "创建时间不能为空")
    private Date createTime;
}
