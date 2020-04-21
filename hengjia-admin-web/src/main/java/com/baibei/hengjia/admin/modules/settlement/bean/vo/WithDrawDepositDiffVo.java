package com.baibei.hengjia.admin.modules.settlement.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithDrawDepositDiffVo extends BaseRowModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 恒价系统订单号
     */
    @ExcelProperty(value = "交易网流水号", index = 1)
    private String orderNo;

    /**
     * 银行系统订单号
     */
    @ExcelProperty(value = "银行前置流水号", index = 2)
    private String externalNo;

    /**
     * 恒价系统订单状态
     */
    @ExcelProperty(value = "交易网订单状态", index = 13)
    private String hengjiaStatus;

    /**
     * 银行订单状态
     */
    @ExcelProperty(value = "银行订单状态", index = 14)
    private String bankStatus;

    /**
     * 恒价系统订单金额
     */
    @ExcelProperty(value = "平台金额", index = 10)
    private BigDecimal hengjiaAmount;

    /**
     * 银行系统订单金额
     */
    @ExcelProperty(value = "银行金额", index = 11)
    private BigDecimal bankAmount;

    /**
     * 出入金订单类型，withdraw=出金，deposit=入金
     */
    @ExcelProperty(value = "类型", index = 12)
    private String type;

    /**
     * 对账差异类型，long_diff=长款差错（银行有，系统没有），short_diff=短款差错（系统有，银行没有），amount_diff=金额不一致，status_diff=状态不一致
     */
    @ExcelProperty(value = "对账结果", index = 15)
    private String diffType;

    /**
     * 处理状态，wait=待处理，deal=已处理
     */
    @ExcelProperty(value = "处理状态", index = 16)
    private String status;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "对账时间", index = 18)
    private String createTime;

    /**
     * 修改时间
     */
    private String modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 交易网会员代码
     */
    @ExcelProperty(value = "交易会员代码", index = 3)
    private String customerNo;

    /**
     * 付款人账号
     */
    @ExcelProperty(value = "付款人账号", index = 4)
    private String payer;
    /**
     * 收款人账号
     */
    @ExcelProperty(value = "收款人账号", index = 5)
    private String payee;
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
    /**
     * 支付手续费子账户
     */
    @ExcelProperty(value = "支付手续费账号", index = 8)
    private String payFeeAccount;
    /**
     * 支付子账户名称
     */
    @ExcelProperty(value = "支付子账号名称", index = 9)
    private String paySubAccountName;
    /**
     * 交易时间
     */
    @ExcelProperty(value = "交易时间", index = 17)
    private String payTime;
}