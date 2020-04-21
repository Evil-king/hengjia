package com.baibei.hengjia.api.modules.cash.dao;

import com.baibei.hengjia.api.modules.cash.model.TempWithdraw;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface TempWithdrawMapper extends MyMapper<TempWithdraw> {

    void posscessList(List<String> orderList);
}