package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.model.TradeDay;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TradeDayMapper extends MyMapper<TradeDay> {
    TradeDay selectTheFifthTradeDay(String currentTradeDay);

    TradeDay selectCurrentTradeDay(String currentDay);

    List<Date> listTradeDay(Map<String,Object> map);

    List<TradeDay> selectDate(@Param("time") Date time, @Param("flag") String valid);

    TradeDay selectTradeDay(@Param("tradeDay") String tradeDay, @Param("period") String period);
}