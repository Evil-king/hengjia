package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.TradeDay;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.Date;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/03 19:41:27
 * @description: TradeDay服务接口
 */
public interface ITradeDayService extends Service<TradeDay> {

    /**
     * 查询当前时间是否可交易时间
     *
     * @return
     */
    boolean isTradeTime();

    /**
     * 设置开休市状态
     */
    boolean setTradeDayToRedis();

    /**
     * 查询交易日
     *
     * @param tradeDay
     * @param period
     * @return
     */
    TradeDay findTradeDay(Date tradeDay, String period);

    TradeDay findTradeDay2(Date tradeDay, String period);
    /**
     * 查询当前时间后的第T+5个交易日信息
     *
     * @return
     */
    TradeDay getTheFifthTradeDay();

    /**
     * 查询当前时间是属于哪个交易日
     *
     * @param currentDate 当前日期
     * @return
     */
    TradeDay getCurrentTradeDay(Date currentDate);

    /**
     * 获取当前时间+N之后的交易时间
     *
     * @return
     */
    Date getAddNTradeDay(int n);

    /**
     * 查看指定时间是否是交易日
     *
     * @param date
     * @return true表示是交易日, false表示非交易日
     */
    boolean isTradeDay(Date date);

    /**
     * 获取指定时间+N之后的交易时间
     *
     * @return
     */
    Date getAddNTradeDay(Date time, int n);

}
