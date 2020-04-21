package com.baibei.hengjia.api.modules.trade.service;

import com.baibei.hengjia.api.modules.trade.model.EntrustDetailsRef;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/10/16 18:41:26
 * @description: EntrustDetailsRef服务接口
 */
public interface IEntrustDetailsRefService extends Service<EntrustDetailsRef> {

    List<EntrustDetailsRef> findByEntrustNo(String entrustNo);

}
