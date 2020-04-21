package com.baibei.hengjia.api.modules.cash.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class WeiPayDto extends CustomerBaseDto {

    @NotNull(message = "入金金额不能为空")
    private BigDecimal amount;

    private String ip;

    private String templeStr;
}
