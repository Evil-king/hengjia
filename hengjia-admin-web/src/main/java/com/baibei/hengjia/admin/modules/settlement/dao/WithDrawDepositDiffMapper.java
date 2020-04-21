package com.baibei.hengjia.admin.modules.settlement.dao;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.WithDrawDepositDiffDto;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.WithDrawDepositDiffVo;
import com.baibei.hengjia.admin.modules.settlement.model.WithDrawDepositDiff;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface WithDrawDepositDiffMapper extends MyMapper<WithDrawDepositDiff> {
    List<WithDrawDepositDiffVo> myList(WithDrawDepositDiffDto withDrawDepositDiffDto);
}