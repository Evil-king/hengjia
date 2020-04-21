package com.baibei.hengjia.api.modules.shop.dao;

import com.baibei.hengjia.api.modules.shop.model.ShpOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.math.BigDecimal;
import java.util.Map;

public interface ShpOrderMapper extends MyMapper<ShpOrder> {

    BigDecimal sumAmount(Map<String, Object> param);

}