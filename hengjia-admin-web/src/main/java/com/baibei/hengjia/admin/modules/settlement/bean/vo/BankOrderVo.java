package com.baibei.hengjia.admin.modules.settlement.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BankOrderVo extends BaseRowModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 1)
    private Integer serialNo;

    /**
     * 记账标志(1：出金 2：入金 3：挂账)
     */
    @ExcelProperty(value = "记账标志", index = 2)
    private String type;

    /**
     * 处理标志(挂账才有值)
     */
    private String dealFlag;

    /**
     * 付款人账号
     */
    @ExcelProperty(value = "付款人账号", index = 3)
    private String payer;

    /**
     * 收款人账号
     */
    @ExcelProperty(value = "收款人账号", index = 4)
    private String payee;

    /**
     * 交易网会员代码
     */
    @ExcelProperty(value = "交易网会员代码", index = 5)
    private String memberNo;

    /**
     * 子账户
     */
    @ExcelProperty(value = "子账户", index = 6)
    private String subAccount;

    /**
     * 子账户名称
     */
    @ExcelProperty(value = "子账户名称", index = 7)
    private String subAccountName;

    @ExcelProperty(value = "真实姓名",index = 8)
    private String realName;

    /**
     * 交易金额
     */
    @ExcelProperty(value = "交易金额", index = 9)
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 支付手续费子账号
     */
    @ExcelProperty(value = "支付手续费子账号", index = 10)
    private String payFeeAccount;

    /**
     * 支付子账号名称
     */
    @ExcelProperty(value = "支付子账户名称", index = 11)
    private String paySubAccountName;

 /*   *//**
     * 交易日期
     *//*
    private String payDate;*/

    /**
     * 交易时间
     */
    @ExcelProperty(value = "交易时间", index = 12)
    private String payTime;

    /**
     * 银行前置流水号
     */
    @ExcelProperty(value = "银行前置流水号", index = 13)
    private String bankSerialNo;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;
}