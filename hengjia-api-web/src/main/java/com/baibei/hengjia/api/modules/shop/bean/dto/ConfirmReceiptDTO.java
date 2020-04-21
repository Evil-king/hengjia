package com.baibei.hengjia.api.modules.shop.bean.dto;

import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ConfirmReceiptDTO extends CustomerBaseDto {

    @NotNull(message = "订单号不能为空")
    private String orderNum;
}
