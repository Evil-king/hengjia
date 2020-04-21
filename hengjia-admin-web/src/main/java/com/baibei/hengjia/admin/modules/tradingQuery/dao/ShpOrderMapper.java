package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.ShopOrderDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.LookOrderDetailVo;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.ShopOrderVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.ShpOrder;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface ShpOrderMapper extends MyMapper<ShpOrder> {

    List<ShopOrderVo> pageList(ShopOrderDto shopOrderDto);

    List<LookOrderDetailVo> lookInfo(String orderNo);

    List<ShopOrderVo> export(ShopOrderDto shopOrderDto);

    int selectByOrderNoExist(String orderNo);

}