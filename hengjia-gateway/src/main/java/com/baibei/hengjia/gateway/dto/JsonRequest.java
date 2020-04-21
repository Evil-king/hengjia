package com.baibei.hengjia.gateway.dto;

import com.baibei.hengjia.common.tool.page.PageParam;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/29 9:54 PM
 * @description:
 */
@Data
public class JsonRequest implements Serializable {
    // 请求唯一标识
    private String requestId;
    // 平台标识，Android、IOS、H5
    private String platform;
    // 设备ID
    private String deviceId;
    // 客户端应用key
    private String appKey;
    // 当前时间的时间戳
    private String timestamp;
    // 版本号
    private String version;
    // 签名
    private String sign;
    // 访问令牌
    private String accessToken;
    // 业务参数数据
    private Map<String, Object> data = new HashMap<String, Object>();
    // 分页参数数据
    private PageParam page;

}
