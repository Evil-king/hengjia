package com.baibei.hengjia.api.modules.match.dao;

import com.baibei.hengjia.api.modules.match.model.MatchPlanDetail;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MatchPlanDetailMapper extends MyMapper<MatchPlanDetail> {
    List<MatchPlanDetail> findbyPlanId(@Param("id") Long id, @Param("status") String status);
}