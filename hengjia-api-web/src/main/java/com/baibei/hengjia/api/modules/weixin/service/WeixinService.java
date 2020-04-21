package com.baibei.hengjia.api.modules.weixin.service;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.api.modules.cash.bean.vo.JsSDK;
import com.baibei.hengjia.api.modules.weixin.bean.AccessToken;
import com.baibei.hengjia.api.modules.weixin.bean.JsapiTicket;
import com.baibei.hengjia.api.modules.weixin.util.WeixinSignUtil;
import com.baibei.hengjia.api.modules.weixin.util.WeixinUtil;
import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.constants.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/17 6:28 PM
 * @description:
 */
@Service
@Slf4j
public class WeixinService {
    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.appsecret}")
    private String appSecret;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * @return
     */
    public void updateWxInfo() {
        log.info("正在更新微信授权信息...");
        //根据微信信息，更新access_token
        AccessToken accessToken = WeixinUtil.getAccessToken(appId, appSecret);
        log.info("accessToken={}",JSON.toJSONString(accessToken));
        JsapiTicket jsapiTicket = null;
        if (accessToken != null) {
            // 获取二维码凭证
            jsapiTicket = WeixinUtil.getJsapiTicket(accessToken.getToken());
            Map<String, Object> cache = new HashMap<>();
            cache.put("accessToken", accessToken.getToken());
            cache.put("jsapiTicket", jsapiTicket == null ? "" : jsapiTicket.getTicket());
            String key = MessageFormat.format(RedisConstant.WX_INFO, appId);
            redisUtil.hsetAll(key, cache);
            redisUtil.expire(key, 7200);
            log.info("更新微信授权信息完毕,cache={}", JSON.toJSONString(cache));
        }
        log.info("更新微信授权信息失败,accessToken={}", JSON.toJSONString(accessToken));
    }

    /**
     * 获取JSSDK签名信息
     *
     * @param url
     * @return
     */
    public JsSDK jssdkInfo(String url) {
        String key = MessageFormat.format(RedisConstant.WX_INFO, appId);
        Object jsapiTicket = redisUtil.hmget(key, "jsapiTicket");
        if (StringUtils.isEmpty(jsapiTicket)) {
            return null;
        }
        JsSDK result = WeixinSignUtil.sign(appId, jsapiTicket.toString(), url);
        return result;
    }
}
