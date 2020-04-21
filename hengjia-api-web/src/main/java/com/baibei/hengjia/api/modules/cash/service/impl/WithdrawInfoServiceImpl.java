package com.baibei.hengjia.api.modules.cash.service.impl;

import com.baibei.hengjia.api.modules.cash.dao.WithdrawInfoMapper;
import com.baibei.hengjia.api.modules.cash.model.WithdrawInfo;
import com.baibei.hengjia.api.modules.cash.service.IWithdrawInfoService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: wenqing
* @date: 2019/06/14 10:56:09
* @description: WithdrawInfo服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class WithdrawInfoServiceImpl extends AbstractService<WithdrawInfo> implements IWithdrawInfoService {

    @Autowired
    private WithdrawInfoMapper tblTempWithdrawInfoMapper;

}
