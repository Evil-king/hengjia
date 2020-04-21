package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.TradeBannerImageMapper;
import com.baibei.hengjia.api.modules.trade.model.TradeBannerImage;
import com.baibei.hengjia.api.modules.trade.service.ITradeBannerImageService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;


/**
* @author: Longer
* @date: 2019/06/03 16:24:23
* @description: TradeBannerImage服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeBannerImageServiceImpl extends AbstractService<TradeBannerImage> implements ITradeBannerImageService {

    @Autowired
    private TradeBannerImageMapper tblTraBannerImageMapper;

}
