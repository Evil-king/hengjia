package com.baibei.hengjia.api.modules.cash.dao;

import com.baibei.hengjia.api.modules.cash.model.OrderWithdraw;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderWithdrawMapper extends MyMapper<OrderWithdraw> {
    List<OrderWithdraw> selectPeriodOrderListNotInBankOrders(String period);

    List<OrderWithdraw> selectPeriodOrderList(String period);

    List<CustomerCountVo> sumFee(Map<String, Object> param);

    List<OrderWithdraw> selectList(List<String> orderNo);

    BigDecimal sumAmountOfCustomer(@Param("customerNo") String customerNo, @Param("nowTime") Date nowTime);

}