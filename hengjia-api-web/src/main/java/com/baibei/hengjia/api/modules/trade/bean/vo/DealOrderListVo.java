package com.baibei.hengjia.api.modules.trade.bean.vo;

import lombok.Data;

/**
 * @author: hyc
 * @date: 2019/6/6 14:10
 * @description:
 */
@Data
public class DealOrderListVo extends  DealOrderVo{
    /**
     * 成交类型
     */
    private String type;
    /**
     *委托方交易编码
     */
    private String customerNo;
    /**
     * 委托订单本票或是配票
     */
    private String entrustHoldType;
    /**
     * 成交订单本票或是配票
     */
    private String dealHoldType;
    /**
     * 商品唯一编码
     */
    private String spuNo;
    /**
     * 买方ID
     */
    private String buyCustomerNo;
    /**
     * 卖方ID
     */
    private String sellCustomerNo;
}
