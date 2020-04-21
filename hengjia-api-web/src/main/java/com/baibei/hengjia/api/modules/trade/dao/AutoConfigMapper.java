package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.model.AutoConfig;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AutoConfigMapper extends MyMapper<AutoConfig> {
    List<AutoConfig> closingTimeList();

    List<AutoConfig> getValidList();
}