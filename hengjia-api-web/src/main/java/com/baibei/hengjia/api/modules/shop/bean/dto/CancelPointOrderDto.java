package com.baibei.hengjia.api.modules.shop.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class CancelPointOrderDto {
    private List<String> orderNo;
}
