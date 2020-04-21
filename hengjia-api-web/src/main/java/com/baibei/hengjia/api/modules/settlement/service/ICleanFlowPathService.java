package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.CleanFlowPath;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/19 11:27:52
 * @description: CleanFlowPath服务接口
 */
public interface ICleanFlowPathService extends Service<CleanFlowPath> {

    void updateStatus(Long id, String status);

    CleanFlowPath findByParam(String batchNo, String projectCode);

    void findAndUpdate(String batchNo, String projectCode, String status);
}
