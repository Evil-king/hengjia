package com.baibei.hengjia.common.tool.enumeration;

/** 冻结资金可用余额流水交易类型
 * @author: hyc
 * @date: 2019/5/29 14:53
 * @description:
 */
public enum FreezingAmountTradeTypeEnum {
    COMMISSION_FREEZE(new Byte("101"),"分佣冻结"),
    LIST_BUY_FREEZE(new Byte("102"),"挂牌买入冻结"),
    DELIST_BUY_FREEZE(new Byte("103"),"挂牌买入解冻"),
    REVOKE_BUY_FREEZE(new Byte("104"),"挂买撤单解冻"),
    REVOKE_DETUCT(new Byte("105"),"配票扣减余额")
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

    FreezingAmountTradeTypeEnum(byte code, String msg) {
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
        for (FreezingAmountTradeTypeEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() == code) {
                return resultCodeMsg.getMsg();
            }
        }
        return null;
    }
}
