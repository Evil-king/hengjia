package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldTotalDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldTotalVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.HoldTotal;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface HoldTotalMapper extends MyMapper<HoldTotal> {

    List<HoldTotalVo> findUserByHoldTotal(HoldTotalDto holdTotalDto);

}