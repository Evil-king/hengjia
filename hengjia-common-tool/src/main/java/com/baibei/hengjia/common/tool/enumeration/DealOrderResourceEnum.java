package com.baibei.hengjia.common.tool.enumeration;

import org.springframework.util.StringUtils;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/23 10:15
 * @description:
 */
public enum DealOrderResourceEnum {
    NORMAL("normal", "普通交易"),
    AUTO_TRADE("auto_trade", "预约单"),;

    private String code;
    private String desc;

    DealOrderResourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 根据code码获取描述信息
     *
     * @param code
     * @return
     */
    public static String getDescByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (DealOrderResourceEnum item : values()) {
            if (code.equals(item.code)) {
                return item.desc;
            }
        }
        return null;
    }

}