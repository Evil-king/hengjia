package com.baibei.hengjia.api.modules.trade.service;
import com.baibei.hengjia.api.modules.trade.model.TradeBanner;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: Longer
* @date: 2019/06/03 15:37:24
* @description: TradeBanner服务接口
*/
public interface ITradeBannerService extends Service<TradeBanner> {

    TradeBanner getMemberBanner(String memberNo);

    /**
     * 2019-06-15 目前系统就1个banner
     * @return
     */
    TradeBanner getSysBanner();
}
