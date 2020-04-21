package com.baibei.hengjia.admin.modules.tradingQuery.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

@Data
public class ShopOrderVo extends BaseRowModel {
    @ExcelProperty(value = "订单号", index = 1)
    private String orderNo;
    @ExcelProperty(value = "客户编号", index = 2)
    private String customerNo;
    @ExcelProperty(value = "注册手机", index = 3)
    private String phone;
    @ExcelProperty(value = "用户名", index = 4)
    private String userName;
    @ExcelProperty(value = "所属挂牌商", index = 5)
    private String memberNo;
    @ExcelProperty(value = "直接推荐", index = 6)
    private String recommenderId;
    @ExcelProperty(value = "订单积分", index = 7)
    private String sumPoint;
    @ExcelProperty(value = "收货人", index = 8)
    private String receiveName;
    @ExcelProperty(value = "收货地址", index = 9)
    private String address;
    @ExcelProperty(value = "联系方式", index = 10)
    private String mobile;
    @ExcelProperty(value = "物流", index = 11)
    private String logisticsCompany;
    @ExcelProperty(value = "订单状态", index = 12)
    private String status;
    @ExcelProperty(value = "物流单号", index = 13)
    private String logisticsNo;
    @ExcelProperty(value = "下单时间", index = 14)
    private String startTime;
    @ExcelProperty(value = "收货时间", index = 15)
    private String endTime;
    @ExcelProperty(value = "商品名称", index = 16)
    private String productName;
    @ExcelProperty(value = "单价", index = 17)
    private String price;
    @ExcelProperty(value = "数量", index = 18)
    private String count;
    @ExcelProperty(value = "消费积分", index = 19)
    private String jifen;
}
