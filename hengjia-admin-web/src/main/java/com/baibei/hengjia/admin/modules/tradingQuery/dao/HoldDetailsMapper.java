package com.baibei.hengjia.admin.modules.tradingQuery.dao;

import com.baibei.hengjia.admin.modules.tradingQuery.bean.dto.HoldDetailsDto;
import com.baibei.hengjia.admin.modules.tradingQuery.bean.vo.HoldDetailsVo;
import com.baibei.hengjia.admin.modules.tradingQuery.model.HoldDetails;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface HoldDetailsMapper extends MyMapper<HoldDetails> {

    /**
     * 查询所有的用户持仓汇总
     *
     * @param holdDetailsDto
     * @return
     */
    List<HoldDetailsVo> findUserHoldDetails(HoldDetailsDto holdDetailsDto);

}