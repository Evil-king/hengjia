package com.baibei.hengjia.admin.modules.dataStatistics.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: hyc
 * @date: 2019/8/15 13:56
 * @description:
 */
@Data
public class CustomerIntegralVo extends BaseRowModel {
    @ExcelProperty(value = "序号", index = 1)
    private Long id;
    /**
     * 用户账号
     */
    @ExcelProperty(value = "用户账号", index = 2)
    private String customerNo;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 3)
    private String username;
    /**
     * 实名名称
     */
    @ExcelProperty(value = "实名名称", index = 4)
    private String realname;
    /**
     * 积分获取数量
     */
    @ExcelProperty(value = "积分获取数量", index = 5)
    private BigDecimal getIntegral;
    /**
     * 积分消费数量
     */
    @ExcelProperty(value = "积分消费数量", index = 6)
    private BigDecimal consumeIntegral;
    /**
     * 交易手续费
     */
    @ExcelProperty(value = "交易手续费", index = 7)
    private BigDecimal tradeFee;
    /**
     * 出金手续费
     */
    @ExcelProperty(value = "出金手续费", index = 8)
    private BigDecimal withdrawFee;
}
