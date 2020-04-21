package com.baibei.hengjia.api.modules.cash.dao;

import com.baibei.hengjia.api.modules.cash.model.OrderDeposit;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface OrderDepositMapper extends MyMapper<OrderDeposit> {
    List<OrderDeposit> selectPeriodOrderList(String period);

    List<OrderDeposit> selectPeriodOrderListNotInBankOrders(String period);
}