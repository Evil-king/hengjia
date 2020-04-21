package com.baibei.hengjia.api.modules.settlement.dao;

import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.settlement.model.CleanHelp;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface CleanHelpMapper extends MyMapper<CleanHelp> {
    List<CustomerCountVo> sumLoss(Map<String, Object> param);

    List<CustomerCountVo> sumProfit(Map<String, Object> param);
}