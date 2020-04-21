package com.baibei.hengjia.api.modules.trade.service;
import com.baibei.hengjia.api.modules.trade.model.TradeAddress;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: Longer
* @date: 2019/06/03 15:37:24
* @description: TradeAddress服务接口（用户地址管理接口）
*/
public interface ITradeAddressService extends Service<TradeAddress> {

    /**
     * 用户地址管理接口
     * @param address
     */
    void saveOrUpdate(TradeAddress address);
}
