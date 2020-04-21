package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/27 17:08:02
 * @description: BatFailResult服务接口
 */
public interface IBatFailResultService extends Service<BatFailResult> {

    List<BatFailResult> findByBatchNo(String batchNo);

}
