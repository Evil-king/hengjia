package com.baibei.hengjia.api.modules.shop.service.impl;

import com.baibei.hengjia.api.modules.shop.dao.ShpLogisticsMapper;
import com.baibei.hengjia.api.modules.shop.model.ShpLogistics;
import com.baibei.hengjia.api.modules.shop.service.IShpLogisticsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: wenqing
* @date: 2019/06/03 15:49:31
* @description: ShpLogistics服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpLogisticsServiceImpl extends AbstractService<ShpLogistics> implements IShpLogisticsService {

    @Autowired
    private ShpLogisticsMapper tblShpLogisticsMapper;

}
