package com.baibei.hengjia.api.modules.match.dao;

import com.baibei.hengjia.api.modules.match.model.ExchangeLog;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface ExchangeLogMapper extends MyMapper<ExchangeLog> {
    Integer selectCurrentDayExchangeCount(@Param("customerNo") String customerNo, @Param ("productTradeNo") String productTradeNo);
}