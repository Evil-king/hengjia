package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class DealOrderDto extends PageParam {
    private String dealNo;//成交单号
    private String type;//成交类型
    private String sellCustomerNo;//卖出方
    private String buyCustomerNo;//买入方
    private String productTradeNo;//商品交易编码
    private String entrustNo;//关联委托单
    private String startTime;
    private String endTime;
}
