package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.DeliveryDetailsMapper;
import com.baibei.hengjia.api.modules.trade.model.DeliveryDetails;
import com.baibei.hengjia.api.modules.trade.service.IDeliveryDetailsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: Longer
* @date: 2019/06/05 10:46:05
* @description: DeliveryDetails服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryDetailsServiceImpl extends AbstractService<DeliveryDetails> implements IDeliveryDetailsService {

    @Autowired
    private DeliveryDetailsMapper tblTraDeliveryDetailsMapper;

}
