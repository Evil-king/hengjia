package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.bean.dto.MyHoldDto;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyDeliveryHoldVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyHoldNewVo;
import com.baibei.hengjia.api.modules.trade.bean.vo.MyHoldVo;
import com.baibei.hengjia.api.modules.trade.model.HoldTotal;
import com.baibei.hengjia.common.core.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HoldTotalMapper extends MyMapper<HoldTotal> {

    List<MyHoldVo> myHoldList(MyHoldDto myHoldDto);

    List<HoldTotal> selectMatchHoldList(String customerNo);

    void updateTotalCount(Map map);

    MyDeliveryHoldVo selectMyDeliveryHold(@Param("customerNo") String customerNo, @Param("productTradeNo") String productTradeNo, @Param("holdType") String holdType);

    List<MyHoldNewVo> myHoldListForNew(Map<String, Object> param);
}