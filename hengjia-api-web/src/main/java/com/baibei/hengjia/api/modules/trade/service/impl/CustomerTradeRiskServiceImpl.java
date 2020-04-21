package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.CustomerTradeRiskMapper;
import com.baibei.hengjia.api.modules.trade.model.CustomerTradeRisk;
import com.baibei.hengjia.api.modules.trade.service.ICustomerTradeRiskService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/03 12:58:48
 * @description: CustomerTradeRisk服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerTradeRiskServiceImpl extends AbstractService<CustomerTradeRisk> implements ICustomerTradeRiskService {

    @Autowired
    private CustomerTradeRiskMapper tblTraCustomerTradeRiskMapper;

    @Override
    public CustomerTradeRisk findByCustomerNo(String customerNo) {
        Condition condition = new Condition(CustomerTradeRisk.class);
        buildValidCriteria(condition).andEqualTo("customerNo",customerNo);
        return findOneByCondition(condition);
    }
}
