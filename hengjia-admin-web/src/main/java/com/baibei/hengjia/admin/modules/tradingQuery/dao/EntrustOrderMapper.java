package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.EntrustOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.EntrustOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface EntrustOrderMapper extends MyMapper<EntrustOrder> {

    List<EntrustOrderVo> findByEntrustOrder(EntrustOrderDto entrustOrderDto);
}