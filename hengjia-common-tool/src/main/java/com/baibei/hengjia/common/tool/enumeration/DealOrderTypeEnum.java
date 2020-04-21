package com.baibei.hengjia.common.tool.enumeration;

/**
 * @author: hyc
 * @date: 2019/5/27 15:02
 * @description:    账号状态
 */
public enum DealOrderTypeEnum {
    MAIN_BUY("mainbuy","零售商品买入"),
    MAIN_SELL("mainsell","零售商品卖出"),
    MATCH_BUY("matchbuy","折扣商品买入"),
    MATCH_SELL("matchsell","折扣商品卖出"),
    MAIN_EXCHANGE("mainexchange","零售商品兑换"),
    MAIN_PAYMENT("mainpayment","零售商品兑付"),
    TRANSFER("transfer","商品置换"),
    OFFSET_BUY("offsetbuy","零售商品买入"),
    OFFSET_SELL("offsetsell","零售商品卖出"),
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

    DealOrderTypeEnum(String code, String msg) {
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
        for (DealOrderTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode().equals(code)) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
    /**
     * 根据状态码获取到对应的提示信息
     *
     * @param msg
     * @return
     */
    public static String getCode(String msg) {
        for (DealOrderTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg().equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return null;
    }

}
