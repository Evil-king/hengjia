package com.baibei.hengjia.api.modules.user.service.impl;

import com.baibei.hengjia.api.modules.user.dao.CustomerDetailMapper;
import com.baibei.hengjia.api.modules.user.model.CustomerDetail;
import com.baibei.hengjia.api.modules.user.service.ICustomerDetailService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: hyc
* @date: 2019/06/03 16:01:58
* @description: CustomerDetail服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerDetailServiceImpl extends AbstractService<CustomerDetail> implements ICustomerDetailService {

    @Autowired
    private CustomerDetailMapper tblCustomerDetailMapper;

    @Override
    public CustomerDetail findByCustomerNo(String customerNo) {
        Condition condition=new Condition(CustomerDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerDetail> customerDetails=tblCustomerDetailMapper.selectByCondition(condition);
        return customerDetails.size()<1?null:customerDetails.get(0);
    }
}
