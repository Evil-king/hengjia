package com.baibei.hengjia.api.modules.cash.bean.vo;

import lombok.Data;

@Data
public class WeiXinVo {

    private String prepay_id;//预支付交易会话标识

    private long timeStamp;//时间戳

    private String appid;//公众账号ID

    private String nonce_str;//随机字符串

    private String sign;//签名

    private String signType;//MD5

}
