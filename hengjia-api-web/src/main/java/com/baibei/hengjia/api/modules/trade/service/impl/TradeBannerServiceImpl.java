package com.baibei.hengjia.api.modules.trade.service.impl;

import com.baibei.hengjia.api.modules.trade.dao.TradeBannerMapper;
import com.baibei.hengjia.api.modules.trade.model.TradeBanner;
import com.baibei.hengjia.api.modules.trade.service.ITradeBannerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* @author: Longer
* @date: 2019/06/03 15:37:24
* @description: TradeBanner服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeBannerServiceImpl extends AbstractService<TradeBanner> implements ITradeBannerService {

    @Autowired
    private TradeBannerMapper bannerMapper;

    @Override
    public TradeBanner getMemberBanner(String memberNo) {
        return bannerMapper.selectMemberBanner(memberNo);
    }

    @Override
    public TradeBanner getSysBanner() {
        List<TradeBanner> tradeBanners=bannerMapper.selectSysBanner();
        return tradeBanners.size()==0?null:tradeBanners.get(0);
    }
}
