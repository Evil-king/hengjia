package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerCountVo;
import com.baibei.hengjia.api.modules.trade.model.MatchLog;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MatchLogMapper extends MyMapper<MatchLog> {
    List<String> selectCustomerNos();

    List<CustomerCountVo> sumFee(Map<String, Object> param);

    List<CustomerCountVo> sumLoss(Map<String, Object> param);

    BigDecimal sumCost(Map<String,Object> param);

}