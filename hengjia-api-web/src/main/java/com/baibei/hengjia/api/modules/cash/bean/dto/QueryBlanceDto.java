package com.baibei.hengjia.api.modules.cash.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QueryBlanceDto{

    @NotNull(message = "查询标志不能为空")
    private String SelectFlag;
}



