package com.baibei.hengjia.common.tool.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/24 4:33 PM
 * @description:
 */
public class NoUtil {
    public static void main(String[] args) {
        System.out.println(getDealNo());
    }

    /**
     * 线程安全的日期格式类
     */
    private static ThreadLocal<DateFormat> yyyyMMddHHmmss = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    private static ThreadLocal<DateFormat> MMddHHmmss = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("MMddHHmmss");
        }
    };

    /**
     * 获取委托单号
     *
     * @return
     */
    public static String getEntrustNo() {
        return yyyyMMddHHmmss.get().format(new Date()) + randomNumbers(6);
    }

    /**
     * 成交单单号
     *
     * @return
     */
    public static String getDealNo() {
        return System.currentTimeMillis()/1000 + randomNumbers(5);
    }

    public static String getBuyMatchNo() {
        return System.currentTimeMillis()/1000 + randomNumbers(15);
    }

    public static String randomNumbers(int length) {
        return randomString("0123456789", length);
    }

    public static String randomString(String baseString, int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }
}

