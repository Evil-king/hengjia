package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.AutoBlacklistMapper;
import com.baibei.hengjia.api.modules.trade.model.AutoBlacklist;
import com.baibei.hengjia.api.modules.trade.service.IAutoBlacklistService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/23 11:39:00
 * @description: AutoBlacklist服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AutoBlacklistServiceImpl extends AbstractService<AutoBlacklist> implements IAutoBlacklistService {

    @Autowired
    private AutoBlacklistMapper tblTraAutoBlacklistMapper;

    @Override
    public AutoBlacklist findByParam(String customerNo, String status) {
        Condition condition = new Condition(AutoBlacklist.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("status", status);
        return findOneByCondition(condition);
    }

    @Override
    public boolean isBlackList(String customerNo) {
        AutoBlacklist autoBlacklist = findByParam(customerNo, Constants.ValidStatus.VALID);
        return autoBlacklist != null;
    }
}
