package com.baibei.hengjia.admin.modules.dataStatistics.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/8/27 13:41
 * @description:
 */
@Data
public class DealOrderDataStatisticsVo extends BaseRowModel {
    /**
     * 日期
     */
    @ExcelProperty(value = "日期", index = 1)
    private String createTime;
    /**
     * 用户编号
     */
    @ExcelProperty(value = "用户账号", index = 2)
    private String customerNo;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 3)
    private String username;
    /**
     * 真实姓名
     */
    @ExcelProperty(value = "实名名称", index = 4)
    private String realname;
    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称", index = 5)
    private String productName;
    /**
     * 商品代码
     */
    @ExcelProperty(value = "商品代码", index = 6)
    private String productTradeNo;
    /**
     * 买入数量
     */
    @ExcelProperty(value = "买入数量", index = 7)
    private BigDecimal buyAmount;
    /**
     * 卖出数量
     */
    @ExcelProperty(value = "卖出数量", index = 8)
    private BigDecimal sellAmount;
    /**
     * 总成交量
     */
    @ExcelProperty(value = "总成交量", index = 9)
    private BigDecimal dealAmount;
    /**
     * 手续费
     */
    @ExcelProperty(value = "手续费", index = 10)
    private BigDecimal totalFee;
}
