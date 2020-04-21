package com.baibei.hengjia.common.tool.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/27 4:53 PM
 * @description:
 */
public class DateUtil {
    private DateUtil() {

    }


    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddHHmmssWithLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddHHmmssNoLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMddHHmmss");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddWithLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 线程安全的日期格式类
     */
    public static ThreadLocal<DateFormat> yyyyMMddNoLine = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    /**
     * 获取指定日期0点
     *
     * @param date
     * @return
     */
    public static Date getBeginDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取当天的0点
     *
     * @return
     */
    public static Date getBeginDay() {
        return getBeginDay(new Date());
    }

    /**
     * 获取指定日期23:59:59秒日期
     *
     * @param date
     * @return
     */
    public static Date getEndDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        return calendar.getTime();
    }

    /**
     * 获取指定日期23:59:59秒日期
     *
     * @return
     */
    public static Date getEndDay() {
        return getEndDay(new Date());
    }

    /**
     * 拼接指定的时分秒,年月日为当前时间
     *
     * @param hhmmss
     * @return
     */
    public static Date appendHhmmss(String hhmmss) {
        return appendHhmmss(new Date(), hhmmss);
    }

    /**
     * 拼接指定的时分秒,年月日为当前时间
     *
     * @param date
     * @param hhmmss
     * @return
     */
    public static Date appendHhmmss(Date date, String hhmmss) {
        StringBuffer sb = new StringBuffer(yyyyMMddWithLine.get().format(date)).append(" ").append(hhmmss);
        try {
            return yyyyMMddHHmmssWithLine.get().parse(sb.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取当天中午12点的时间
     *
     * @return
     */
    public static Date getMiddleTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取从现在开始的一天
     *
     * @return
     */
    public static String get1DayByNowTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return yyyyMMddHHmmssWithLine.get().format(cal.getTime());
    }

    /**
     * 获取当前日期基础上增加指定天数后的日期
     *
     * @param day
     * @return
     */
    public static Date addDay(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 功能描述：时间相减得到天数
     *
     * @param bDate 开始时间
     * @param eDate 结束时间
     * @return
     * @author Longer
     */
    public static long getDaySub(Date bDate, Date eDate) {
        String beginDateStr = yyyyMMddWithLine.get().format(bDate);
        String endDateStr = yyyyMMddWithLine.get().format(eDate);
        long day = 0;
        Date beginDate;
        Date endDate;
        try {
            beginDate = yyyyMMddWithLine.get().parse(beginDateStr);
            endDate = yyyyMMddWithLine.get().parse(endDateStr);
            day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
            //System.out.println("相隔的天数="+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }


    /**
     * 判断传入时间是否是当天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        return yyyyMMddNoLine.get().format(date).equals(yyyyMMddNoLine.get().format(new Date()));
    }
}
