package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.DealHoldRefMapper;
import com.baibei.hengjia.api.modules.trade.model.DealHoldRef;
import com.baibei.hengjia.api.modules.trade.service.IDealHoldRefService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 19:41:27
* @description: DealHoldRef服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class DealHoldRefServiceImpl extends AbstractService<DealHoldRef> implements IDealHoldRefService {

    @Autowired
    private DealHoldRefMapper tblTraDealHoldRefMapper;

}
