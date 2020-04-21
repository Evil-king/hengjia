package com.baibei.hengjia.admin.modules.settlement.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustDzFailVo  extends BaseRowModel {
    /**
     * 主键
     */
    private Long id;

    /**
     * 批次号
     */
    @ExcelProperty(value = "结算日", index = 1)
    private String batchNo;

    /**
     * 子账户账号
     */
    @ExcelProperty(value = "子账户账号", index = 2)
    private String custAcctId;

    /**
     * 子账户名称
     */
    @ExcelProperty(value = "子账户名称", index = 3)
    private String custName;

    /**
     * 交易网会员代码
     */
    @ExcelProperty(value = "交易网会员代码", index = 4)
    private String thirdCustId;

    /**
     * 银行清算后可用余额
     */
    @ExcelProperty(value = "银行清算后可用余额", index = 5)
    private BigDecimal bankBalance;

    /**
     * 银行清算后冻结余额
     */
    @ExcelProperty(value = "银行清算后冻结余额", index = 6)
    private BigDecimal bankFrozenAmount;

    /**
     * 交易网可用余额
     */
    @ExcelProperty(value = "交易网可用余额", index = 7)
    private BigDecimal hengjiaBalance;

    /**
     * 交易网冻结余额
     */
    @ExcelProperty(value = "交易网冻结余额", index = 8)
    private BigDecimal hengjiaFrozenAmount;

    /**
     * 可用余额差额（银行可用余额-交易网可用余额）
     */
    @ExcelProperty(value = "可用余额差额", index = 9)
    private BigDecimal balanceDiff;

    /**
     * 冻结余额差额（银行冻结余额-交易网冻结余额）
     */
    @ExcelProperty(value = "冻结余额差额", index = 10)
    private BigDecimal frozenDiff;

    /**
     * 对账结果说明
     */
    @ExcelProperty(value = "对账结果说明", index = 11)
    private String remark;
}
