package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class ShopOrderDto extends PageParam {
    private String orderNo;
    private String status;
    private String customerNo;
    private String userName;
    private String memberNo;
    private String logisticsNo;
    private String startTime;
    private String endTime;
}
