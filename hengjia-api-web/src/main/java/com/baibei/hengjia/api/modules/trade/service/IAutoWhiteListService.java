package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.AutoWhiteList;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/09 18:59:40
 * @description: AutoWhiteList服务接口
 */
public interface IAutoWhiteListService extends Service<AutoWhiteList> {

    AutoWhiteList findByParam(String customerNo, String status);

    boolean isWhiteList(String customerNo);
}
