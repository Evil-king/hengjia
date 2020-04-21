package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.bean.dto.DeliveryQueryDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryVo;
import com.baibei.hengjia.api.modules.trade.model.Delivery;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DeliveryMapper extends MyMapper<Delivery> {
    List<MyDeliveryVo> selectForPage(DeliveryQueryDto queryDto);

    Delivery selectTheFirstDelivery(String customerNo);

    BigDecimal sumAmount(Map<String, Object> param);
}