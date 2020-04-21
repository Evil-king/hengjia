package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.model.RecordFreezingAmount;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerFrozenVo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface RecordFreezingAmountMapper extends MyMapper<RecordFreezingAmount> {

    List<CustomerFrozenVo> sumCustomerFrozenList(Map<String, Object> params);
}