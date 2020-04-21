package com.baibei.hengjia.admin.modules.withdraw.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawListVo extends BaseRowModel {
    @ExcelProperty(value = "订单号", index = 1)
    private String orderNo;
    @ExcelProperty(value = "用户编码", index = 2)
    private String customerNo;
    @ExcelProperty(value = "用户名", index = 3)
    private String userName;
    @ExcelProperty(value = "手机号", index = 4)
    private String mobile;
    @ExcelProperty(value = "提现金额", index = 5)
    private BigDecimal orderamt;
    @ExcelProperty(value = "手续费", index = 6)
    private BigDecimal handelFee;
    @ExcelProperty(value = "状态", index = 7)
    private String status;
    @ExcelProperty(value = "所属挂牌商", index = 8)
    private String memberNo;
    @ExcelProperty(value = "申请时间", index = 9)
    private String startTime;
    @ExcelProperty(value = "更新时间", index = 10)
    private String endTime;
    @ExcelProperty(value = "审核人", index = 11)
    private String reviewer;
    @ExcelProperty(value = "审核时间", index = 12)
    private String reviewTime;
    @ExcelProperty(value = "银行流水号", index = 13)
    private String externalNo;
}
