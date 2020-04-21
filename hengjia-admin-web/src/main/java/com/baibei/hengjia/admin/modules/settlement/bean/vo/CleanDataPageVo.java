package com.baibei.hengjia.admin.modules.settlement.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/8 3:52 PM
 * @description:
 */
@Data
public class CleanDataPageVo extends BaseRowModel {
    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 序号
     */
    private Integer queryId;

    /**
     * 交易时间
     */
    @ExcelProperty(value = "序号", index = 1)
    private String tranDateTime;

    /**
     * 操作员号
     */
    private String counterId;

    /**
     * 资金汇总账号
     */
    @ExcelProperty(value = "资金汇总账号", index = 2)
    private String supacctId;

    /**
     * 功能代码
     */
    private String funcCode;

    /**
     * 子账户账号
     */
    @ExcelProperty(value = "子账户账号", index = 3)
    private String custAcctId;

    /**
     * 子账户名称
     */
    @ExcelProperty(value = "子账户名称", index = 4)
    private String custName;

    /**
     * 交易网会员代码
     */
    @ExcelProperty(value = "交易网会员代码", index = 5)
    private String thirdCustId;

    /**
     * 交易网流水号
     */
    @ExcelProperty(value = "交易网流水号", index = 6)
    private String thirdLogNo;

    /**
     * 币种
     */
    private String ccyCode;

    /**
     * 当天总冻结金额
     */
    @ExcelProperty(value = "当天总冻结金额", index = 7)
    private BigDecimal freezeAmount;

    /**
     * 当天总解冻金额
     */
    @ExcelProperty(value = "当天总解冻金额", index = 8)
    private BigDecimal unfreezeAmount;

    /**
     * 当天成交的总货款（作为卖方）（增加银行可用余额）
     */
    @ExcelProperty(value = "当天成交的总贷款（卖方）", index = 9)
    private BigDecimal addTranAmount;

    /**
     * 当天成交的总货款（作为买方）（减少银行可用余额）
     */
    @ExcelProperty(value = "当天成交的总贷款（买方）", index = 10)
    private BigDecimal cutTranAmount;

    /**
     * 盈利总金额
     */
    @ExcelProperty(value = "盈利总金额", index = 11)
    private BigDecimal profitAmount;

    /**
     * 亏损总金额
     */
    @ExcelProperty(value = "亏损总金额", index = 12)
    private BigDecimal lossAmount;

    /**
     * 当天应支付给交易网的交易手续费总额
     */
    @ExcelProperty(value = "当天应支付给交易网手续费总额", index = 13)
    private BigDecimal tranHandFee;

    /**
     * 当天交易总笔数
     */
    @ExcelProperty(value = "当天总交易笔数", index = 14)
    private BigDecimal tranCount;

    /**
     * 交易网端会员最新的可用余额
     */
    @ExcelProperty(value = "交易网端会员最新可用余额", index = 15)
    private BigDecimal newBalance;

    /**
     * 交易网端会员最新的冻结余额
     */
    @ExcelProperty(value = "交易网端会员最新的冻结余额", index = 15)
    private BigDecimal newFreezeAmount;

    /**
     * 说明
     */
    private String note;

    /**
     * 保留域
     */
    private String reserve;

    /**
     * 创建时间
     */
    private Date createTime;
}
