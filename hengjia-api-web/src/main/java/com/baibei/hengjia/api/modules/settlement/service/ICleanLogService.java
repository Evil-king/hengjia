package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.CleanLog;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/26 20:47:15
 * @description: CleanLog服务接口
 */
public interface ICleanLogService extends Service<CleanLog> {

    CleanLog findByBatchNo(String batchNo);


    boolean updateByBatchNo(String batchNo, String status, String resp);

}
