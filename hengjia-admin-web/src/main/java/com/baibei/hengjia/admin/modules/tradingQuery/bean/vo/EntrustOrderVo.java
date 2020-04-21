package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import lombok.Data;


import java.math.BigDecimal;


@Data
public class EntrustOrderVo extends BaseRowModel {
    @ExcelProperty(value = "用户账号", index = 1)
    private String customerNo;

    @ExcelProperty(value = "用户名", index = 2)
    private String userName;

    @ExcelProperty(value = "真实姓名", index = 3)
    private String realName;

    /**
     * 推荐人
     */
    @ExcelProperty(value = "直接推荐人", index = 4)
    private String directRecommendation;

    /**
     * 委托时间
     */
    @ExcelProperty(value = "委托时间", index = 5)
    private String entrustTime;

    /**
     * 委托方向
     */
    @ExcelProperty(value = "委托方向", index = 6)
    private String direction;

    /**
     * 商品交易编码
     */
    @ExcelProperty(value = "商品交易编码", index = 7)
    private String productTradeNo;

    /**
     * 商品名
     */
    @ExcelProperty(value = "商品名称", index = 8)
    private String productName;

    /**
     * 委托价格
     */
    @ExcelProperty(value = "委托价格", index = 9)
    private BigDecimal price;

    /**
     * 委托数量
     */
    @ExcelProperty(value = "委托数量", index = 10)
    private Integer entrustCount;

    /**
     * 委托总价值
     */
    @ExcelProperty(value = "委托总价值", index = 11)
    private BigDecimal entrustCountPrice;

    /**
     * 成交数量
     */
    @ExcelProperty(value = "已成交数量", index = 12)
    private Integer dealCount;

    /**
     * 委托单号
     */
    @ExcelProperty(value = "委托单号", index = 13)
    private String entrustNo;

    /**
     * 类型
     */
    @ExcelProperty(value = "持有类型", index = 14)
    private String holdType;

    /**
     * 委托结果
     */
    @ExcelProperty(value = "处理结果", index = 15)
    private String result;

}
