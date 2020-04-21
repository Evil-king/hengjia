package com.baibei.hengjia.admin.modules.settlement.dao;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BatFailResultMapper extends MyMapper<BatFailResult> {

    List<BatFailResult> list(CleanResultDto cleanResultDto);
}