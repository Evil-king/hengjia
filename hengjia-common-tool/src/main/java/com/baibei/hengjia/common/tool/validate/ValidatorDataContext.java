package com.baibei.hengjia.common.tool.validate;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 7:28 PM
 * @description: 数据校验上下文
 */
@Data
public class ValidatorDataContext {
    // 业务校验过程中的数据传递
    private Map<String, Object> dataContextMap = new ConcurrentHashMap<>();

    // 请求参数
    private Object requestData;
}
