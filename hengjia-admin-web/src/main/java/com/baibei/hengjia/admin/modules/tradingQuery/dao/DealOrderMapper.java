package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.DealOrderDataStatisticsDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.DealOrderDataStatisticsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DealOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DealOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.DealOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.List;

public interface DealOrderMapper extends MyMapper<DealOrder> {

    List<DealOrderVo> pageList(DealOrderDto dealOrderDto);

    BigDecimal findTradeFeeByCustomerNo(@Param("customerNo") String customerNo, @Param("startTime")String startTime, @Param("endTime")String endTime);

    BigDecimal findSellMoneyByCustomer(@Param("customerNo") String customerNo, @Param("createTime") String createTime);

    List<DealOrderDataStatisticsVo> dealOrderDataStatisticsVoList(DealOrderDataStatisticsDto dealOrderDataStatisticsDto);
    BigDecimal findSellAmountByCustomerAndDate(@Param("customerNo") String customerNo,@Param("createTime") String time);

    BigDecimal findBuyAmountByCustomerAndDate(@Param("customerNo") String customerNo,@Param("createTime") String time);

    BigDecimal findBuyOrSellTradeFeeByCustomerNo(@Param("customerNo") String customerNo,@Param("createTime") String time);
}