package com.baibei.hengjia.admin.modules.settlement.enums;

public enum CleanFlowPathStatusEnum {
    WAIT("wait", "未进行"),
    COMPLETED("completed", "已完成");

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

    CleanFlowPathStatusEnum(String code, String msg) {
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
        for (CleanFlowPathStatusEnum cleanFlowPathStatusEnum : values()) {
            if (cleanFlowPathStatusEnum.getCode().equals(code)) {
                return cleanFlowPathStatusEnum.getMsg();
            }
        }
        return null;
    }
}
