package com.baibei.hengjia.api.modules.shop.service.impl;

import com.baibei.hengjia.api.modules.shop.dao.ShpOrderDetailsMapper;
import com.baibei.hengjia.api.modules.shop.model.ShpOrderDetails;
import com.baibei.hengjia.api.modules.shop.service.IShpOrderDetailsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: wenqing
* @date: 2019/07/19 10:34:08
* @description: ShpOrderDetails服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class ShpOrderDetailsServiceImpl extends AbstractService<ShpOrderDetails> implements IShpOrderDetailsService {

    @Autowired
    private ShpOrderDetailsMapper tblShpOrderDetailsMapper;

}
