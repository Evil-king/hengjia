package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class HoldDetailsVo extends BaseRowModel {


    private Long id;

    /**
     * 持有流水号
     */
    @ExcelProperty(value = "流水号", index = 2)
    private String holdNo;

    /**
     * 持有明细时间
     */
    @ExcelProperty(value = "持有时间", index = 1)
    private String holdTime;

    /**
     * 商品交易编码
     */
    @ExcelProperty(value = "商品交易编码", index = 3)
    private String productTradeNo;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称", index = 4)
    private String productName;

    @ExcelProperty(value = "类型", index = 6)
    private String holdType;

    /**
     * 状态 1、已扫描代表以及解锁
     * 2、未扫描代表未解锁
     */
    @ExcelProperty(value = "状态", index = 5)
    private String scanner;

    /**
     * 剩余数量
     */
    @ExcelProperty(value = "剩余数量", index = 7)
    private BigDecimal remaindCount;

    /**
     * 冻结数量
     */
    private BigDecimal frozenCount;


    /**
     * 最新价
     */
    @ExcelProperty(value = "商品最新价", index = 8)
    private BigDecimal latestPrice;

    /**
     * 持仓市值(持有数量*最新价)
     */
    @ExcelProperty(value = "持仓市值", index = 9)
    private BigDecimal holdPositionsPrice;

    /**
     * 持有来源，deal=摘牌成交、plan=配售、transfer=非交易过户',
     */
    @ExcelProperty(value = "来源", index = 10)
    private String resource;

}
