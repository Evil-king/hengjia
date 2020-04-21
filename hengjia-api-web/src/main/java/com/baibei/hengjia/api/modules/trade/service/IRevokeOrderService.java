package com.baibei.hengjia.api.modules.trade.service;
import com.baibei.hengjia.api.modules.trade.model.RevokeOrder;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: 会跳舞的机器人
* @date: 2019/06/03 19:41:27
* @description: RevokeOrder服务接口
*/
public interface IRevokeOrderService extends Service<RevokeOrder> {

    void addRevokeOrder(RevokeOrder revokeOrder);
}
