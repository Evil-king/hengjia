package com.baibei.hengjia.common.tool.enumeration;

/** 资金可用余额流水交易类型
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum FundTradeTypeEnum {
    MONEY_INTO(new Byte("101"), "入金"),
    MONEY_OUT(new Byte("102"), "出金"),
    MAIN_BUY(new Byte("103"), "零售商品买入"),
    MAIN_SELL(new Byte("104"), "零售商品卖出"),
    MATCH_BUY(new Byte("105"), "折扣商品买入"),
    MATCH_SELL(new Byte("106"), "折扣商品卖出"),
    ACCOUNT_ADJUSTMENT(new Byte("107"), "调账"),
    MONEY_OUT_FEE(new Byte("108"),"出金手续费"),
    WITHDRAW_BACK(new Byte("109"),"出金手续费回退"),
    MONEY_OUT_BACK(new Byte("110"),"出金回退"),
    ACCOUNT_ADJUSTMENT_ADD(new Byte("111"), "资金支付"),//增加资金
    ACCOUNT_ADJUSTMENT_SUB(new Byte("112"), "资金补扣"),//减少资金
    FEE_SETTLEMENT(new Byte("113"),"手续费结算收入"),
    INTEGRARL_SETTLEMENT(new Byte("114"),"采购专款结算收入"),
    ACCOUNT_ADJUSTMENT_FEE_ADD(new Byte("115"), "资金支付"),//增加资金
    ACCOUNT_ADJUSTMENT_FEE_SUB(new Byte("116"), "资金补扣"),//减少资金
    OFFSET_SELL_FEE(new Byte("117"), "抵扣卖出手续费"),//减少资金
    CREDIT_SETTLEMENT_IN(new Byte("118"), "提货款结算收入"),
    CREDIT_SETTLEMENT_OUT(new Byte("119"), "提货款结算支出"),
    DELIVERYTICKET_IN(new Byte("119"), "提货券结算收入"),
    DELIVERYTICKET_OUT(new Byte("120"), "提货券结算支出"),
    OFFSET_DELIVERYTICKET_OUT(new Byte("121"), "提货券结算支出"),
    OFFSET_DELIVERYTICKET_IN(new Byte("122"), "提货券结算收入"),
    DELIVERY_TICKET_BACK_OUT(new Byte("123"),"提货券金额扣回"),
    DELIVERY_TICKET_BACK_IN(new Byte("124"),"提货券金额回退"),
    MARKET_ADJUST(new Byte("125"),"市场调节"),
    TRANSFER (new Byte("126"),"商品过户"),


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

    FundTradeTypeEnum(byte code, String msg) {
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
        for (FundTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() == code) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
}
