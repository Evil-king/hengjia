package com.baibei.hengjia.api.modules.user.service.impl;

import com.baibei.hengjia.api.modules.user.dao.CustomerRefMapper;
import com.baibei.hengjia.api.modules.user.model.CustomerRef;
import com.baibei.hengjia.api.modules.user.service.ICustomerRefService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: hyc
* @date: 2019/06/03 14:43:33
* @description: CustomerRef服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerRefServiceImpl extends AbstractService<CustomerRef> implements ICustomerRefService {

    @Autowired
    private CustomerRefMapper tblCustomerRefMapper;

}
