package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.model.TradeAddress;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

public interface TradeAddressMapper extends MyMapper<TradeAddress> {
    void updateDefault(TradeAddress address);
}