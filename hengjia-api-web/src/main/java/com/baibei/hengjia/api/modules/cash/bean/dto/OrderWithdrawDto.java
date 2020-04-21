package com.baibei.hengjia.api.modules.cash.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 出金
 */
@Data
@ToString(callSuper=true)
public class OrderWithdrawDto extends CustomerBaseDto {

    @NotNull(message = "转出金额不能为空")
    private BigDecimal orderAmt;

    @NotNull(message = "收款账号不能为空")
    private String receiveAccount;

    @NotNull(message = "收款银行不能为空")
    private String bankName;

    @NotNull(message = "收款账户不能为空")
    private String accountName;

    private String password;//资金密码

    private String type;

    private String status;

    private String externalNo;
}
