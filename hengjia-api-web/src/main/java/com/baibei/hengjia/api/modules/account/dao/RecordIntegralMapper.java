package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.model.RecordIntegral;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface RecordIntegralMapper extends MyMapper<RecordIntegral> {
    List<RecordVo> getAll(RecordDto recordIntegralDto);
}