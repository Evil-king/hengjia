package com.baibei.hengjia.api.modules.weixin.util;

import com.baibei.hengjia.api.modules.cash.bean.vo.JsSDK;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/18 4:51 PM
 * @description: 微信JSSDK签名
 */
public class WeixinSignUtil {

    /**
     * 签名
     *
     * @param appId
     * @param jsapi_ticket
     * @param url
     * @return
     */
    public static JsSDK sign(String appId, String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<>();
        String nonce_str = create_nonce_str();
        long timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsSDK jsSDK = new JsSDK();
        jsSDK.setAppId(appId);
        jsSDK.setNonceStr(nonce_str);
        jsSDK.setSignature(signature);
        jsSDK.setTimestamp(timestamp);
        return jsSDK;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static Long create_timestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
