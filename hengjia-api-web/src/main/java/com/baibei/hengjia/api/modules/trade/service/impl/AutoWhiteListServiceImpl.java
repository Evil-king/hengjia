package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.AutoWhiteListMapper;
import com.baibei.hengjia.api.modules.trade.model.AutoWhiteList;
import com.baibei.hengjia.api.modules.trade.service.IAutoWhiteListService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/09 18:59:40
 * @description: AutoWhiteList服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AutoWhiteListServiceImpl extends AbstractService<AutoWhiteList> implements IAutoWhiteListService {

    @Autowired
    private AutoWhiteListMapper tblTraAutoWhitelistMapper;

    @Override
    public AutoWhiteList findByParam(String customerNo, String status) {
        Condition condition = new Condition(AutoWhiteList.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("customerNo", customerNo);
        criteria.andEqualTo("status", status);
        return findOneByCondition(condition);
    }

    @Override
    public boolean isWhiteList(String customerNo) {
        AutoWhiteList autoWhiteList = findByParam(customerNo, Constants.ValidStatus.VALID);
        return autoWhiteList != null;
    }
}
