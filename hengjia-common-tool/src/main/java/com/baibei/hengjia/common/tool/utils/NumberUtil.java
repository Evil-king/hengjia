package com.baibei.hengjia.common.tool.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @author: 会跳舞的机器人
 * @date: 2017/1/18 9:49
 * @description: 数字计算相关工具类
 */
public class NumberUtil {
    /**
     * 中文金额单位数组
     */
    private static final String[] straChineseUnit = new String[]
            {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};

    /**
     * 中文数字字符数组
     */
    private static final String[] straChineseNumber = new String[]
            {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};


    /**
     * 判断一个对象是否为一个整型
     *
     * @param o
     * @return
     */
    public static boolean isNumber(Object o) {
        Pattern pattern = Pattern.compile("[0-9]*");

        return (o == null || o.toString() == "") ? false : pattern.matcher(o.toString()).matches();
    }

    /**
     * 将一个对象解析成整型，如果对象不是整型 则返回默认值
     *
     * @param obj
     * @param defaultv
     * @return
     */
    public static Integer parseInt(Object obj, Integer defaultv) {

        return !isNumber(obj) ? defaultv : Integer.parseInt(obj.toString());
    }

    /**
     * 将一个对象解析成整型，如果对象不是整型，则返回0
     *
     * @param obj
     * @return
     */
    public static Integer parseInt(Object obj) {

        return parseInt(obj, 0);
    }

    /**
     * 以指定精度，对指定双精度浮点型数值进行四舍五入
     *
     * @param number 双精度浮点型数值
     * @param digits 精度
     * @return
     */
    public static String decimalFormat(double number, int digits) {
        StringBuffer sb = new StringBuffer("0");
        if (digits > 0) {
            sb.append(".");
        }

        for (int i = 0; i < digits; i++) {
            sb.append("0");
        }

        DecimalFormat df = new DecimalFormat(sb.toString());

        return df.format(number);
    }


    /**
     * 将双精度浮点型数值转换为百分号形式
     *
     * @param number
     * @return
     */
    public static String convertPercentString(double number) {

        return roundDown(number * 100) + "%";
    }

    /**
     * 以指定精度，对指定双精度浮点型数值转换为百分号形式
     *
     * @param number
     * @param digits
     * @return
     */
    public static String convertPercentString(double number, int digits) {

        return decimalFormat(number * 100, digits) + "%";
    }

    /**
     * 加法运算
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 减法运算
     *
     * @param v1 被减数
     * @param v2 减数
     * @return
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 乘法运算
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static BigDecimal mul(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.multiply(b2);
    }

    /**
     * 除法运算，当发生除不尽的情况时，精确到小数点以后2位，以后的数字四舍五入
     *
     * @param v1 被除数
     * @param v2 除数
     * @return
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, 2);
    }

    /**
     * 除法运算，当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 精确到小数点以后几位
     * @return
     */
    public static double div(double v1, double v2, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 四舍五入
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * roundFloor
     *
     * @param d
     * @param scale
     * @return
     */
    public static double roundFloor(double d, int scale) {
        BigDecimal bd = new BigDecimal(d);
        Double result = bd.setScale(scale, BigDecimal.ROUND_FLOOR).doubleValue();
        return result;
    }

    /**
     * roundFloor
     *
     * @param d
     * @param scale
     * @return
     */
    public static BigDecimal roundFloor(BigDecimal d, int scale) {
        return d.setScale(scale, BigDecimal.ROUND_FLOOR);
    }

    /**
     * 判断 a 与 b 是否相等
     *
     * @param a
     * @param b
     * @return a==b 返回true, a!=b 返回false
     */
    public static boolean equal(double a, double b) {
        BigDecimal v1 = BigDecimal.valueOf(a);
        BigDecimal v2 = BigDecimal.valueOf(b);
        if (v1.compareTo(v2) == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断 a 是否大于等于 b
     *
     * @param a
     * @param b
     * @return a&gt;=b 返回true, a&lt;b 返回false
     */
    public static boolean greaterThanOrEqualTo(double a, double b) {
        BigDecimal v1 = BigDecimal.valueOf(a);
        BigDecimal v2 = BigDecimal.valueOf(b);
        if (v1.compareTo(v2) >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断 a 是否大于 b
     *
     * @param a
     * @param b
     * @return a&gt;b 返回true, a&lt;=b 返回 false
     */
    public static boolean bigger(double a, double b) {
        BigDecimal v1 = BigDecimal.valueOf(a);
        BigDecimal v2 = BigDecimal.valueOf(b);
        if (v1.compareTo(v2) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 判断 a 是否小于 b
     *
     * @param a
     * @param b
     * @return a&lt;b 返回true, a&gt;=b 返回 false
     */
    public static boolean lessThan(double a, double b) {
        BigDecimal v1 = BigDecimal.valueOf(a);
        BigDecimal v2 = BigDecimal.valueOf(b);
        if (v1.compareTo(v2) == -1) {
            return true;
        }
        return false;
    }

    /**
     * 四舍五入保留小数点后两位
     *
     * @param num
     * @return
     */
    public static double roundDown(double num) {

        return Double.valueOf(String.format("%.2f", num));
    }


    public static String getRandomID (){
        Random r = new Random();
        StringBuffer sb = new StringBuffer(10);

        for (int j = 1; j <= 10; j++) {
            int i = r.nextInt(10);
            if (j == 1 || (j >= 8 && j <= 10)) {//防止第一位和最后三位的值为0
                while (i == 0) {
                    i = r.nextInt(10);
                }
            }

            sb.append(i);
        }
        return sb.toString();
    }

}