package com.baibei.hengjia.admin.modules.customer.service.impl;

import com.baibei.hengjia.admin.modules.customer.dao.CustomerIntegralMapper;
import com.baibei.hengjia.admin.modules.customer.model.CustomerIntegral;
import com.baibei.hengjia.admin.modules.customer.service.ICustomerIntegralService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: hyc
* @date: 2019/07/16 14:39:31
* @description: CustomerIntegral服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerIntegralServiceImpl extends AbstractService<CustomerIntegral> implements ICustomerIntegralService {

    @Autowired
    private CustomerIntegralMapper tblCustomerIntegralMapper;

    @Override
    public CustomerIntegral selectByCustomerNo(String customerNo) {
        Condition condition=new Condition(CustomerIntegral.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        List<CustomerIntegral> customerIntegrals=tblCustomerIntegralMapper.selectByCondition(condition);
        if(customerIntegrals.size()>0){
            return customerIntegrals.get(0);
        }else{
            return null;
        }
    }
}
