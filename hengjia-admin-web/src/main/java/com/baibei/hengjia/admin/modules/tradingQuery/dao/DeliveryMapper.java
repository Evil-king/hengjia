package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.DeliveryDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryExportVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.DeliveryVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.Delivery;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface DeliveryMapper extends MyMapper<Delivery> {
    List<DeliveryVo> myList(DeliveryDto deliveryDto);

    List<DeliveryExportVo> exportList(DeliveryDto deliveryDto);

    int selectByOrderNoExist(String deliveryNo);

}