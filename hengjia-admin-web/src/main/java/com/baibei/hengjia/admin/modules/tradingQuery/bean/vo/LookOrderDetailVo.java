package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LookOrderDetailVo {
    private String productName;
    private BigDecimal price;
    private int count;
    private String integral;
}
