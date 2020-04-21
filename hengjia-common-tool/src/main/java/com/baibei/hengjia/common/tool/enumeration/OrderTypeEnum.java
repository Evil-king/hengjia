package com.baibei.hengjia.common.tool.enumeration;

/**
 * 订单类型
 */
public enum OrderTypeEnum {

    /**
     * 入金
     */
    DEPOSIT("deposit"),

    /**
     * 出金
     */
    WITHDRAW("withdraw");


    OrderTypeEnum(String orderType) {
        this.orderType = orderType;
    }

    private String orderType;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
