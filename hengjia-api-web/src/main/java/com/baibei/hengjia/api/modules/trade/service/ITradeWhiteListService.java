package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.TradeWhiteList;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/17 15:48:14
 * @description: TradeWhiteList服务接口
 */
public interface ITradeWhiteListService extends Service<TradeWhiteList> {


    boolean isWhiteList(String customerNo, String type);

}
