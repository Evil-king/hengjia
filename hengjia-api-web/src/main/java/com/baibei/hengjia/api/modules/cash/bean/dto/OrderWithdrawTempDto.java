package com.baibei.hengjia.api.modules.cash.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2019/06/13
 * <p>
 *     出金临时方案入参
 * </p>
 */
@Data
public class OrderWithdrawTempDto extends CustomerBaseDto {

    @NotNull(message = "转出金额不能为空")
    private BigDecimal amount;

    @NotNull(message = "银行账户不能为空")
    private String bankAccount;

    @NotNull(message = "开户银行不能为空")
    private String accountBank;

    @NotNull(message = "开户支行不能为空")
    private String branchBank;

    @NotNull(message = "银行卡号不能为空")
    private String bankNum;
}
