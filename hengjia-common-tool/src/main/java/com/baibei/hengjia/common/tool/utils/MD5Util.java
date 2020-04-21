package com.baibei.hengjia.common.tool.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * @author: 会跳舞的机器人
 * @date: 2017/9/18 15:08
 * @description: MD5工具类
 */
public class MD5Util {
    public static void main(String[] args) {
        String salt = getRandomSalt();
        String pwd = md5Hex("111111",salt);
        System.out.println(salt);
        System.out.println(pwd);
    }
    private static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * MD5加密
     *
     * @param plaintext 密码
     * @param salt      盐值
     * @return 密文
     */
    public static String md5Hex(String plaintext, String salt) {
        return DigestUtils.md5Hex(plaintext + salt);
    }

    /**
     * 微信用的
     * @param origin
     * @param charsetname
     * @return
     */
    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charsetname == null || "".equals(charsetname))
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes()));
            else
                resultString = byteArrayToHexString(md.digest(resultString
                        .getBytes(charsetname)));
        } catch (Exception exception) {
            System.out.println("加密错误!");
        }
        return resultString;
    }

    /**
     * MD5加密
     *
     * @param plaintext 密码
     * @return 密文
     */
    public static String md5Hex(String plaintext) {
        return DigestUtils.md5Hex(plaintext);
    }

    /**
     * 获取12位的随机盐值
     */
    public static String getRandomSalt() {
        return getRandomSalt(12);
    }

    /**
     * 获取指定位数的随机盐值
     *
     * @param num 位数
     * @return 随机盐值
     */
    public static String getRandomSalt(final int num) {
        final StringBuilder saltString = new StringBuilder();
        for (int i = 1; i <= num; i++) {
            saltString.append(B64T.charAt(new SecureRandom().nextInt(B64T.length())));
        }
        return saltString.toString();
    }


    private static final String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String encode(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] byteArray = md5.digest(password.getBytes("utf-8"));
            String passwordMD5 = byteArrayToHexString(byteArray);
            return passwordMD5;
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        return password;
    }

    private static String byteArrayToHexString(byte[] byteArray) {
        StringBuffer sb = new StringBuffer();
        for (byte b : byteArray) {
            sb.append(byteToHexChar(b));
        }
        return sb.toString();
    }

    private static Object byteToHexChar(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hex[d1] + hex[d2];
    }
}
