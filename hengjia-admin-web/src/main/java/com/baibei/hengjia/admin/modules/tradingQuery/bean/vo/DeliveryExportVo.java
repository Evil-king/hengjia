package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class DeliveryExportVo extends BaseRowModel {
    @ExcelProperty(value = "提货单号", index = 1)
    private String deliveryNo;
    @ExcelProperty(value = "用户编号", index = 2)
    private String customerNo;
    @ExcelProperty(value = "手机号", index = 3)
    private String mobile;
    @ExcelProperty(value = "申请时间", index = 4)
    private String deliveryTime;
    @ExcelProperty(value = "商品交易编码", index = 5)
    private String productTradeNo;
    @ExcelProperty(value = "产品名称", index = 6)
    private String productTradeName;
    @ExcelProperty(value = "商品类型", index = 7)
    private String holdType;
    @ExcelProperty(value = "提货数量", index = 8)
    private String deliveryCount;
    @ExcelProperty(value = "提货价", index = 9)
    private String issuePrice;
    @ExcelProperty(value = "发货时间", index = 10)
    private String pendingTime;
    @ExcelProperty(value = "物流公司", index = 11)
    private String logisticsCompany;
    @ExcelProperty(value = "物流单号", index = 12)
    private String logisticsNo;
    @ExcelProperty(value = "收货时间", index = 13)
    private String receiveTime;
    @ExcelProperty(value = "仓储费", index = 14)
    private String storageCharge;
    @ExcelProperty(value = "出仓费", index = 15)
    private String clearanceCharge;
    @ExcelProperty(value = "运费", index = 16)
    private String freight;
    @ExcelProperty(value = "收货人", index = 17)
    private String receivingName;
    @ExcelProperty(value = "收货地址", index = 18)
    private String receivingAddress;
    @ExcelProperty(value = "联系方式", index = 19)
    private String receivingMobile;
    @ExcelProperty(value = "状态", index = 20)
    private String deliveryStatus;
    @ExcelProperty(value = "审核人", index = 21)
    private String auditer;
    @ExcelProperty(value = "审核时间", index = 22)
    private String auditTime;
    @ExcelProperty(value = "备注", index = 23)
    private String remark;
}
