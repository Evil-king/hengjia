package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.bean.vo.AutoTradeRecordVo;
import com.baibei.hengjia.api.modules.trade.model.AutoRecord;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface AutoRecordMapper extends MyMapper<AutoRecord> {

    List<AutoTradeRecordVo> pageList(Map<String, Object> params);
}