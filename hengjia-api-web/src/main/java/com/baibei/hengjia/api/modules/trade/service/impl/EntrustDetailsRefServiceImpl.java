package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.EntrustDetailsRefMapper;
import com.baibei.hengjia.api.modules.trade.model.EntrustDetailsRef;
import com.baibei.hengjia.api.modules.trade.service.IEntrustDetailsRefService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/16 18:41:26
 * @description: EntrustDetailsRef服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EntrustDetailsRefServiceImpl extends AbstractService<EntrustDetailsRef> implements IEntrustDetailsRefService {

    @Autowired
    private EntrustDetailsRefMapper tblTraEntrustDetailsRefMapper;

    @Override
    public List<EntrustDetailsRef> findByEntrustNo(String entrustNo) {
        Condition condition = new Condition(EntrustDetailsRef.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("entrustNo", entrustNo);
        return findByCondition(condition);
    }
}
