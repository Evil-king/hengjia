package com.baibei.hengjia.api.modules.cash.bean.vo;

import lombok.Data;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/17 5:19 PM
 * @description:
 */
@Data
public class JsSDK {
    // 必填，公众号的唯一标识
    private String appId;
    // 必填，生成签名的时间戳
    private Long timestamp;
    // 必填，生成签名的随机串
    private String nonceStr;
    // 必填，签名
    private String signature;
}
