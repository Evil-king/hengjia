package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.trade.bean.dto.DealOrderDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.DealOrderListVo;
import com.baibei.hengjia.api.modules.trade.model.DealOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DealOrderMapper extends MyMapper<DealOrder> {
    List<DealOrderListVo> findByCustomerNo(DealOrderDto dealOrderDto);

    Integer selectCountByDate(@Param("customerNo") String customerNo, @Param("date") String format);

    List<CustomerCountVo> sumMoneyForBuy(Map<String, Object> param);

    List<CustomerCountVo> sumMoneyForSell(Map<String, Object> param);

    List<CustomerCountVo> sumFee(Map<String, Object> param);

    List<CustomerCountVo> tradeCount(Map<String, Object> param);

    List<CustomerCountVo> sumProfitAmountForSell(Map<String, Object> param);

    List<CustomerCountVo> sumLossAmountForBuy(Map<String, Object> param);

    BigDecimal sumAllFee(Map<String, Object> param);

    BigDecimal sumAllIntegral(Map<String, Object> param);

    Integer sumBuyCount(Map<String, Object> param);

    Integer sumSellCount(Map<String, Object> param);

    List<String> findTradeProductNo(@Param("customerNo") String customerNo, @Param("time") String time);

    List<DealOrder> sumIntegral(@Param("currentDate") String currentDate);

    Integer buyCountToday(Map<String, Object> params);

    BigDecimal sumAllCouponAmount(Map<String, Object> param);

    BigDecimal sumAllHongmuFund(Map<String, Object> param);

    List<DealOrder> querySellList(Map<String, Object> param);

    List<CustomerCountVo> sumHongmuFund(Map<String,Object> param);


}