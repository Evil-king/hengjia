package com.baibei.hengjia.common.tool.enumeration;



import java.util.HashMap;
import java.util.Map;

/**
 * 审核的状态通用常量
 */
public enum ReviewStatusEnum {

    // 待审核
    UNAUDIT("unaudit"),

    // 审核通过
    PASSED("passed"),

    // 审核驳回
    REJECTED("rejected"),

    // 超时
    TIMEOUT("timeout");

    private static final Map<String, ReviewStatusEnum> statusMap = new HashMap<>(ReviewStatusEnum.values().length);

    
    static {
        for (ReviewStatusEnum result : ReviewStatusEnum.values()) {
            statusMap.put(result.getStatus(), result);
        }
    }

    ReviewStatusEnum(String status) {
        this.status = status;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static ReviewStatusEnum getStatusEnum(String status) {
        return statusMap.get(status);
    }


}
