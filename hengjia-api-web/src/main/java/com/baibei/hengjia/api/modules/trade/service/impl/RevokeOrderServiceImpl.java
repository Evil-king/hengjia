package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.RevokeOrderMapper;
import com.baibei.hengjia.api.modules.trade.model.RevokeOrder;
import com.baibei.hengjia.api.modules.trade.service.IRevokeOrderService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 19:41:27
* @description: RevokeOrder服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class RevokeOrderServiceImpl extends AbstractService<RevokeOrder> implements IRevokeOrderService {

    @Autowired
    private RevokeOrderMapper tblTraRevokeOrderMapper;

    @Override
    public void addRevokeOrder(RevokeOrder revokeOrder) {
        tblTraRevokeOrderMapper.insert(revokeOrder);
    }
}
