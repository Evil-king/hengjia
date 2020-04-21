package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.model.HoldDetails;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface HoldDetailsMapper extends MyMapper<HoldDetails> {

    List<HoldDetails> findByParamTemp(Map<String, Object> param);

    List<HoldDetails> sumRemaindCountByTradeTime(Map<String, Object> param);

    List<HoldDetails> findHoldDetailsForNext(Map<String, Object> param);

    List<HoldDetails> findByDealOrderNO(List<String> dealOrderNoList);


}