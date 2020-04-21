package com.baibei.hengjia.admin.modules.settlement.dao;

import com.baibei.hengjia.admin.modules.settlement.bean.dto.CleanResultDto;
import com.baibei.hengjia.admin.modules.settlement.model.CustDzFail;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface CustDzFailMapper extends MyMapper<CustDzFail> {

    List<CustDzFail> list(CleanResultDto cleanResultDto);
}