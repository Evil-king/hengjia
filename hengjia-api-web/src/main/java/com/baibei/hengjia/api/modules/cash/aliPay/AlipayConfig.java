package com.baibei.hengjia.api.modules.cash.aliPay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AlipayConfig {
    // 商户appid
    @Value("${ALIPAY.APPID}")
    public String APPID ;
    // 私钥 pkcs8格式的
    @Value("${ALIPAY_PRIVATE_KEY}")
    public  String PRIVATE_KEY;
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${ALIPAY_NOTIFY_URL}")
    public  String notify_url;
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
//    @Value("${ALIPAY_RERUEN_URL}")
//    public  String return_url;
    // 请求网关地址
    @Value("${ALIPAY_URL}")
    public  String URL;
    // 支付宝公钥
    @Value("${ALIPAY_PUBLIC_KEY}")
    public  String PUBLIC_KEY;
    // RSA2
    @Value("${ALIPAY_SIGNTYPE}")
    public  String SIGNTYPE;
    // 编码
    @Value("${ALIPAY_CHARSET}")
    public String CHARSET;
    // 返回格式
    @Value("${ALIPAY_FORMAT}")
    public String FORMAT;
    @Value("${ALIPAY_SUBJECT}")
    private String subject;
}
