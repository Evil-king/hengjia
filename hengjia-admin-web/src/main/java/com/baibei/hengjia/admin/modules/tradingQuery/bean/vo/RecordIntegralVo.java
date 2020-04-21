package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/7/15 14:25
 * @description:
 */
@Data
public class RecordIntegralVo extends BaseRowModel {
    private String id;
    @ExcelProperty(value = "用户编号", index = 1)
    private String customerNo;
    @ExcelProperty(value = "用户名", index = 2)
    private String username;
    @ExcelProperty(value = "真实姓名",index = 3)
    private String realName;
    @ExcelProperty(value = "手机号", index = 4)
    private String mobile;
    @ExcelProperty(value = "流水号", index = 5)
    private String recordNo;
    @ExcelProperty(value = "发生时间", index = 6)
    private String createTime;
    @ExcelProperty(value = "项目", index = 7)
    private String tradeType;
    @ExcelProperty(value = "类型", index = 8)
    private String retype;
    @ExcelProperty(value = "积分", index = 9)
    private String changeAmount;
    @ExcelProperty(value = "关联单号", index = 10)
    private String orderNo;
}
