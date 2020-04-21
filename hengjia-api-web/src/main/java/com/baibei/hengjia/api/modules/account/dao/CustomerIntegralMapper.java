package com.baibei.hengjia.api.modules.account.dao;

import com.baibei.hengjia.api.modules.account.bean.dto.IntegralDetailDto;
import com.baibei.hengjia.api.modules.account.bean.vo.IntegralDetailVo;
import com.baibei.hengjia.api.modules.account.model.CustomerIntegral;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

public interface CustomerIntegralMapper extends MyMapper<CustomerIntegral> {
    IntegralDetailVo findIntegralDetailByCustomer(IntegralDetailDto integralDetailDto);
}