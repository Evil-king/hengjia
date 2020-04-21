package com.baibei.hengjia.api.modules.trade.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Classname MatchDto
 * @Description 配票dto
 * @Date 2019/6/10 11:01
 * @Created by Longer
 */
@Data
public class MatchDto {
    /**
     * 配票单号
     */
    @NotBlank(message = "配票单号不能为空")
    private String matchNo;
    /**
     * 用户编号
     */
    @NotBlank(message = "用户编码不能为空")
    private String customerNo;
    /**
     * 配票商品编号
     */
    @NotBlank(message = "交易商品编码不能为空")
    private String productTradeNo;
    /**
     * 配票类型(BUY_MATCH:买入配票；ASSEMBLE_MATCH:拼团配票；GROUP_MATCH:组团配票；SEND:赠送（提货配票）)
     */
    @NotBlank(message = "配票类型不能为空")
    private String matchType;
    /**
     * 配票数量
     */
    @NotNull(message = "配票数量不能为空")
    private BigDecimal matchNum;

    /**
     * 配票状态（SUCCESS：成功；FAIL：失败；HALF_SUCCESS：部分成功）
     */
    private String matchStatus;
    /**
     * 成功配票数量
     */
    private BigDecimal matchSuccessNum;
    /**
     * 配票金额
     */
    private BigDecimal matchMoney;
    /**
     * 配票成本
     */
    private BigDecimal cost;
    /**
     * 配票手续费
     */
    private BigDecimal fee;

    /**
     *备注
     */
    private String remark;

}
