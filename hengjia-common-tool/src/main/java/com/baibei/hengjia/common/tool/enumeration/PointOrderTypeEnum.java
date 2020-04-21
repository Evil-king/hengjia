package com.baibei.hengjia.common.tool.enumeration;


public enum PointOrderTypeEnum {
    /**
     * success:兑换成功(即待发货状态),fail:兑换失败,delivered:已发货(待收货),received:已收货
     */
    POINT_ORDER_SUCCESS("success","待发货"),
    POINT_ORDER_WAITING("delivered","待收货"),
    POINT_ORDER_RECEIVED("received","已收货"),
    ;

    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    PointOrderTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据状态码获取到对应的提示信息
     *
     * @param code
     * @return
     */
    public static String getMsg(String code) {
        for (PointOrderTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode().equalsIgnoreCase(code)) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
}
