package com.baibei.hengjia.api.modules.cash.aliPay;

import java.io.UnsupportedEncodingException;

/**
 * @author hwq
 * @date 2018/11/12
 */
public class UrlDecode {
    private final static String ENCODE = "UTF-8";

    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
