package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class IntegralDto extends BaseRowModel {
    @ExcelProperty(value = "订单号", index = 0)
    private String orderNo;
    @ExcelProperty(value = "物流单号", index = 1)
    private String logisticsNo;
    @ExcelProperty(value = "物流公司", index = 2)
    private String logisticsCompany;
}
