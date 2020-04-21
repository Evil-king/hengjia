package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoldTotalVo extends BaseRowModel {

    private Long id;

    @ExcelProperty(value = "用户账号", index = 1)
    private String customerNo;

    @ExcelProperty(value = "商品交易编码", index = 2)
    private String userName;

    @ExcelProperty(value = "手机号码", index = 3)
    private String phoneNumber;

    @ExcelProperty(value = "真实姓名", index = 3)
    private String realName;

    @ExcelProperty(value = "商品交易编号", index = 4)
    private String productTradeNo;
    /**
     * 直接推荐
     */
    @ExcelProperty(value = "直接推荐人", index = 5)
    private String directRecommendation;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "产品名称", index = 6)
    private String productName;

    /**
     * 持仓类型
     */
    @ExcelProperty(value = "持仓类型", index = 7)
    private String holdType;

    /**
     * 商品数量
     */
    @ExcelProperty(value = "商品数量", index = 8)
    private BigDecimal productNumber;

    /**
     * 最新价
     */
    @ExcelProperty(value = "最新价", index = 9)
    private BigDecimal latestPrice;

    /**
     * 持仓市值(持有数量*最新价)
     */
    @ExcelProperty(value = "持仓价", index = 10)
    private BigDecimal holdPositionsPrice;

    /**
     * 冻结数量
     */
    @ExcelProperty(value = "冻结数量",index = 11)
    private BigDecimal frozenCount;

    /**
     * 可卖数量
     */
    @ExcelProperty(value = "可卖数量",index = 12)
    private BigDecimal canSellCount;
}
