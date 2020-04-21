package com.baibei.hengjia.api.modules.match.dao;

import com.baibei.hengjia.api.modules.match.model.MatchFailLog;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;


public interface MatchFailLogMapper extends MyMapper<MatchFailLog> {
    int update(MatchFailLog matchFailLog);

    List<MatchFailLog> selectCurrentDayFailLogs(String status);
}