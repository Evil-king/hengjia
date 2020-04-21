package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.BuyMatchWhiteList;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/12 15:34:23
 * @description: BuyMatchWhiteList服务接口
 */
public interface IBuyMatchWhiteListService extends Service<BuyMatchWhiteList> {

    BuyMatchWhiteList findByParam(String customerNo, String status);

    boolean isWhiteList(String customerNo);
}
