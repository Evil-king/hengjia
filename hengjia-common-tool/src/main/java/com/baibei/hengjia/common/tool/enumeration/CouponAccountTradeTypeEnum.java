package com.baibei.hengjia.common.tool.enumeration;

/**
 * @author: hyc
 * @date: 2019/8/5 10:58
 * @description:
 */
public enum CouponAccountTradeTypeEnum {
    MAIN_EXCHANGE(new Byte("101"),"折扣商品兑换"),
    ACTIVITY_GIVING(new Byte("102"),"活动赠送"),//配券拼团赠送
    SELL_REWARD(new Byte("103"),"销售奖励"),//配券组团赠送
    PRODUCT_REPLACE(new Byte("104"),"商品置换"),
    OFFSET_CONSUME(new Byte("105"),"抵扣消费"),
    DELIVERY_TICKET_GET(new Byte("106"),"获得提货券"),
    DELIVERY_TICKET_CONSUMPTION(new Byte("107"),"提货券消费"),
    COUPON_THAW(new Byte("108"),"兑换券获取"),
    //以下是冻结券金额所用的交易类型
    FROZENAMOUT_IN(new Byte("109"),"待奖励收入"),
    FROZENAMOUT_OUT(new Byte("110"),"奖励"),
    //以下是解冻券金额所用的交易类型
    THAWAMOUT_IN(new Byte("111"),"获得额度"),
    THAWAMOUT_OUT(new Byte("112"),"消耗额度"),
    DELIVERY_TICKET_BACK(new Byte("113"),"扣回提货券"),
    DELIVERY(new Byte("114"),"交割提货"),
    ;

    private byte code;
    private String msg;

    public byte getCode() {
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

    CouponAccountTradeTypeEnum(byte code, String msg) {
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
        for (CouponAccountTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() == code) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
    /**
     * 根据对应的信息获取状态码
     *
     * @param msg
     * @return
     */
    public static byte getCode(String msg) {
        for (CouponAccountTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return 0;
    }
}
