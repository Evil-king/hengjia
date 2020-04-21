package com.baibei.hengjia.admin.modules.dataStatistics.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/8/22 14:29
 * @description:
 */
@Data
public class FundDataStatisticsVo extends BaseRowModel {
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
     * 期初资金
     */
    @ExcelProperty(value = "期初资金", index = 5)
    private BigDecimal initMoney;
    /**
     * 入金金额
     */
    @ExcelProperty(value = "银行入金", index = 6)
    private BigDecimal rechargeMoney;
    /**
     * 销售收入
     */
    @ExcelProperty(value = "销售收入", index = 7)
    private BigDecimal sellMoeny;
    /**
     * 出金金额
     */
    @ExcelProperty(value = "银行出金", index = 8)
    private BigDecimal withdrawMoney;
}
