package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.model.MatchConfig;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.Map;

public interface MatchConfigMapper extends MyMapper<MatchConfig> {
    int updateSwitch(Map map);
}