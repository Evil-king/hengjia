package com.baibei.hengjia.settlement.service.impl;

import com.baibei.hengjia.settlement.dao.WithDrawDepositDiffMapper;
import com.baibei.hengjia.settlement.model.WithDrawDepositDiff;
import com.baibei.hengjia.settlement.service.IWithDrawDepositDiffService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: Longer
* @date: 2019/06/25 11:30:00
* @description: WithDrawDepositDiff服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WithDrawDepositDiffServiceImpl extends AbstractService<WithDrawDepositDiff> implements IWithDrawDepositDiffService {

    @Autowired
    private WithDrawDepositDiffMapper withDrawDepositDiffMapper;

}
