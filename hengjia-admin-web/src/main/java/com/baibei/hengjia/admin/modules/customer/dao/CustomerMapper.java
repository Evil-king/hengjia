package com.baibei.hengjia.admin.modules.customer.dao;

import com.baibei.hengjia.admin.modules.customer.bean.dto.CustomerDto;
import com.baibei.hengjia.admin.modules.customer.bean.vo.CustomerVo;
import com.baibei.hengjia.admin.modules.customer.model.Customer;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.dto.CustomerIntegralDto;
import com.baibei.hengjia.admin.modules.dataStatistics.bean.vo.CustomerIntegralVo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface CustomerMapper extends MyMapper<Customer> {
    List<CustomerVo> pageList(CustomerDto customerDto);

    /**
     * 用户积分+手续费统计
     * @param customerIntegralDto
     * @return
     */
    List<CustomerIntegralVo> integralPageList(CustomerIntegralDto customerIntegralDto);
}