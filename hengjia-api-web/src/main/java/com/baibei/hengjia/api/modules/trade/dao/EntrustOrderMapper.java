package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.bean.dto.EntrustAllListDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.EntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyEntrustOrderVo;
import com.baibei.hengjia.api.modules.trade.model.EntrustOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import com.baibei.hengjia.common.tool.bean.CustomerBaseAndPageDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface EntrustOrderMapper extends MyMapper<EntrustOrder> {

    List<EntrustOrderVo> allList(EntrustAllListDto entrustAllListDto);

    List<MyEntrustOrderVo> myList(CustomerBaseAndPageDto customerBaseAndPageDto);

    BigDecimal entrustCount(Map<String,Object> param);

    /**
     * 查询所有可以撤销的委托单
     * @return
     */
    List<EntrustOrder> allRevokeList(Map params);

    List<EntrustOrder> findNotDealEntrustOrder(Map<String,Object> params);
}