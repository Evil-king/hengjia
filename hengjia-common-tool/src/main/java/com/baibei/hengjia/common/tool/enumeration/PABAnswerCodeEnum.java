package com.baibei.hengjia.common.tool.enumeration;

/**
 * 平安银行应答码
 */
public enum PABAnswerCodeEnum {

    SUCCESS("000000", "交易成功"),

    ERR940("ERR940", "法律冻结帐户"),

    ERR941("ERR941", "帐户止付"),

    ERR942("ERR942", "帐户密码挂失"),

    ERR943("ERR943", "帐户存折已正式挂失"),

    ERR944("ERR944", "帐户存折已口头挂失"),

    ERR947("ERR947", "转换失败"),

    ERR952("ERR951", "取交易码出错"),

    ERR957("ERR957", "交易异常,请稍候查询交易结果"),

    ERR021("ERR021", "错误的功能码"),

    ERR023("ERR023", "结算账户已开户"),

    ERR024("ERR024", "结算账户未开户"),

    ERR026("ERR026", "输入交易网代码错误"),

    ERR031("ERR031", "输入的子账号错误"),

    ERR033("ERR033", "输入到出入金账户不存在或与子账号不匹配"),

    ERR034("ERR034", "输入的出入金账号已存在"),

    ERR039("ERR039", "该出入金账号已销户"),

    ERR042("ERR042", "金额不能小于或等于0"),

    ERR044("ERR044", "可用余额不足"),

    ERR045("ERR045", "转入和转出子账户必须同属一个交易网"),

    ERR074("ERR074", "交易网返回失败描述"),

    ERR041("ERR041","该子账号不存在");


    PABAnswerCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //描述码
    private String code;

    //描述消息
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


}
