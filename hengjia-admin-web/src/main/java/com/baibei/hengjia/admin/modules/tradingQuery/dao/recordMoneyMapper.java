package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.RecordMoneyDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.RecordMoneyVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.RecordMoney;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface recordMoneyMapper extends MyMapper<RecordMoney> {
    List<RecordMoneyVo> pageList(RecordMoneyDto recordMoneyDto);

    BigDecimal findSumByDateAndCustomerNoAndTradeType(@Param("customerNo") String customerNo, @Param("tradeType") String tradeType,@Param("startTime")String startTime, @Param("endTime")String endTime);

}