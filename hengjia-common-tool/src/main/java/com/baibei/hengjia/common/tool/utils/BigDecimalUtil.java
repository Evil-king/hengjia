package com.baibei.hengjia.common.tool.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author hwq
 * @date 2018/11/04
 */
public class BigDecimalUtil {
    /**
     * 计算小数乘法，向下取整
     *
     * @param percent 小数百分比
     * @param value   整数
     * @return 返回向下取整的整数
     */
    public static long multiplyPercent(String percent, long value) {
        if (StringUtils.isEmpty(percent)) {
            throw new IllegalArgumentException("percent为空");
        }
        BigDecimal bigDecimal = new BigDecimal(percent);
        if (BigDecimal.ONE.compareTo(bigDecimal) < 0
                || BigDecimal.ZERO.compareTo(bigDecimal) >= 0) {
            throw new IllegalArgumentException("decimal must in [0,1]");
        }
        return bigDecimal.multiply(BigDecimal.valueOf(value)).setScale(1, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 计算小数乘法，保留两位小数向上取整
     *  @param percent 小数百分比
     *  @param value   整数
     * @return
     */
    public static BigDecimal multiplyPercent1(String percent, BigDecimal value) {
        if (StringUtils.isEmpty(percent)) {
            throw new IllegalArgumentException("percent为空");
        }
        BigDecimal bigDecimal = new BigDecimal(percent);
        if (BigDecimal.ONE.compareTo(bigDecimal) < 0
                || BigDecimal.ZERO.compareTo(bigDecimal) >= 0) {
            throw new IllegalArgumentException("decimal must in [0,1]");
        }
        return bigDecimal.multiply(value).setScale(2, BigDecimal.ROUND_UP);
    }

    /**
     * 计算小数乘法，保留两位小数直接截取
     *  @param percent 小数百分比
     *  @param value   整数
     * @return
     */
    public static BigDecimal multiplyPercent2(String percent, BigDecimal value) {
        if (StringUtils.isEmpty(percent)) {
            throw new IllegalArgumentException("percent为空");
        }
        BigDecimal bigDecimal = new BigDecimal(percent);
        if (BigDecimal.ONE.compareTo(bigDecimal) < 0
                || BigDecimal.ZERO.compareTo(bigDecimal) >= 0) {
            throw new IllegalArgumentException("decimal must in [0,1]");
        }
        return bigDecimal.multiply(value).setScale(2, BigDecimal.ROUND_DOWN);
    }


    /**
     * @param decimal
     * @param value
     * @return
     */
    public static long multiply(String decimal, long value) {
        if (StringUtils.isEmpty(decimal)) {
            throw new IllegalArgumentException("decimal为空");
        }
        BigDecimal bigDecimal = new BigDecimal(decimal);
        return bigDecimal.multiply(BigDecimal.valueOf(value)).setScale(2, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 提供精确的乘法运算。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算,并且四舍五入保留两位小数。
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mul(String v1,String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        double temp = b1.multiply(b2).doubleValue();
        return String.valueOf(round(temp,2));
    }

    /**
     * 提供精确的乘法运算
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static String mulWithOutRound(String v1,String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        String result = b1.multiply(b2).toPlainString();
        return result;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String round(String v,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).toPlainString();
    }

    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static String add(String v1,String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.add(b2).doubleValue());
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static String sub(String v1,String v2){
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return String.valueOf(b1.subtract(b2).doubleValue());
    }


    /**
     * 两个字符串之间的比较
     * @param value
     * @param decimalStr
     * @return
     */
    public static int compareTo(String value, String decimalStr) {
        return new BigDecimal(value).compareTo(new BigDecimal(decimalStr));
    }

    /**
     * 两个BigDecimal之间的比较
     * @param value
     * @param decimalStr
     * @return
     */
    public static int compareToBigDecimal(BigDecimal value, BigDecimal decimalStr) {
        return value.compareTo(decimalStr);
    }
}
