package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.dao.CouponOffsetMapper;
import com.baibei.hengjia.api.modules.match.model.CouponOffset;
import com.baibei.hengjia.api.modules.match.service.ICouponOffsetService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: Longer
* @date: 2019/08/21 15:41:59
* @description: CouponOffset服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class CouponOffsetServiceImpl extends AbstractService<CouponOffset> implements ICouponOffsetService {

    @Autowired
    private CouponOffsetMapper tblCpCouponOffsetMapper;

}
