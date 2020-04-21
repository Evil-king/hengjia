package com.baibei.hengjia.common.tool.enumeration;

/**
 * @author: hyc
 * @date: 2019/5/27 15:02
 * @description:    账号状态
 */
public enum CustomerStatusEnum {
    NORMAL(100, "正常"),// 账号正常
    LIMIT_LOGIN(101, "限制登录"),// 限制登录
    LIMIT_RECHARGE(102, "限制充值"),// 限制充值
    LIMIT_WITHDRAW(103, "限制提现"),// 限制提现
    LIMIT_BUY(104, "限制买入"),// 限制买入
    LIMIT_SELL(105, "限制卖出"),// 限制卖出
    ;

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    CustomerStatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据状态码获取到对应的提示信息
     *
     * @param code
     * @return
     */
    public static String getMsg(int code) {
        for (CustomerStatusEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getCode() == code) {
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
    public static int getCode(String msg) {
        for (CustomerStatusEnum resultCodeMsg : values()) {
            if (resultCodeMsg.getMsg() .equals(msg) ) {
                return resultCodeMsg.getCode();
            }
        }
        return 0;
    }

}
