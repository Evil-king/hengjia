package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DealOrderVo extends BaseRowModel {
    @ExcelProperty(value = "成交单号", index = 1)
    private String dealNo;//成交单号
    @ExcelProperty(value = "成交时间", index = 2)
    private String createTime;//成交时间
    @ExcelProperty(value = "成交类型", index = 3)
    private String type;//成交类型
    @ExcelProperty(value = "卖出方", index = 4)
    private String sellCustomerNo;//卖出方
    @ExcelProperty(value = "买入方", index = 5)
    private String buyCustomerNo;//买入方
    @ExcelProperty(value = "商品交易编码", index = 6)
    private String productTradeNo;//商品交易编码
    @ExcelProperty(value = "商品名称", index = 7)
    private String productTradeName;//商品名称
    @ExcelProperty(value = "成交价", index = 8)
    private BigDecimal price;//成交价
    @ExcelProperty(value = "成交数量", index = 9)
    private int count;//成交数量
    @ExcelProperty(value = "成交总额", index = 10)
    private BigDecimal sumPrice;//成交总额
    @ExcelProperty(value = "关联委托单", index = 11)
    private String entrustNo;//关联委托单
}
