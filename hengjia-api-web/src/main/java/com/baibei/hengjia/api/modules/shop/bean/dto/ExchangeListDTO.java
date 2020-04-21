package com.baibei.hengjia.api.modules.shop.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExchangeListDTO extends PageParam {

    @NotBlank(message = "客户编码不能为空")
    private String customerNo;

    private String startTime;

    private String endTime;
}
