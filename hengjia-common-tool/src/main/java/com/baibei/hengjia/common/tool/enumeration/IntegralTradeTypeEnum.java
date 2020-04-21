package com.baibei.hengjia.common.tool.enumeration;


/** 积分流水交易类型
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum IntegralTradeTypeEnum {
    EXCHANGE_INTEGRAL_PRODUCT(new Byte("101"),"商城消费"),
    TRADING_GIVING(new Byte("102"),"交易赠送"),
    INTEGRAL_BACK(new Byte("103"),"积分回退"),
    DELIVERY(new Byte("104"),"交割提货"),
    ;

    private byte code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    IntegralTradeTypeEnum(byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据状态码获取到对应的提示信息
     *
     * @param code
     * @return
     */
    public static String getMsg(byte code) {
        for (IntegralTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() == code) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
}
