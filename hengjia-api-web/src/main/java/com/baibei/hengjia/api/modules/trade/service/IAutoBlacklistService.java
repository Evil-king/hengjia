package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.AutoBlacklist;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/09/23 11:39:00
 * @description: AutoBlacklist服务接口
 */
public interface IAutoBlacklistService extends Service<AutoBlacklist> {

    AutoBlacklist findByParam(String customerNo, String status);

    boolean isBlackList(String customerNo);


}
