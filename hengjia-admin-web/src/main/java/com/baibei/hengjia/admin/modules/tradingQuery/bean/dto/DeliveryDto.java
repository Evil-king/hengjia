package com.baibei.hengjia.admin.modules.tradingQuery.bean.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

@Data
public class DeliveryDto extends PageParam {
    /**
     * 提货订单号
     */
    private String deliveryNo;

    /**
     * 提货状态
     */
    private String deliveryStatus;

    /**
     * 商品交易编码
     */
    private String productTradeNo;

    /**
     * 用户编码
     */
    private String customerNo;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String logisticsNo;

    /**
     * 商品类型
     */
    private String holdType;

    /**
     * 来源
     */
    private String source;
}
