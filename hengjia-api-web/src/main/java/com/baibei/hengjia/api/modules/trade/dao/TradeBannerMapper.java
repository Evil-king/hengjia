package com.baibei.hengjia.api.modules.trade.dao;

import com.baibei.hengjia.api.modules.trade.model.TradeBanner;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;


public interface TradeBannerMapper extends MyMapper<TradeBanner> {
    TradeBanner selectMemberBanner(String memberNo);

    List<TradeBanner> selectSysBanner();
}