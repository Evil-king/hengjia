package com.baibei.hengjia.api.modules.shop.bean.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hwq
 * @date 2019/06/04
 */
@Data
public class OrderListVO {

    private String orderNum;//订单号

    private Date createTime;//创建时间

    private BigDecimal sumPoint;//合计积分

    private String logisticsNo;//物流单号

    private String logisticsCompany;//物流公司

    private String orderStatus;//订单状态

    private String statusRemark;//状态描述

    private List<OrderDetailsVO> list;//订单详情集合

}
