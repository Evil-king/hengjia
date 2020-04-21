package com.baibei.hengjia.admin.modules.settlement.service;

import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanFlowPathVo;
import com.baibei.hengjia.admin.modules.settlement.model.CleanFlowPath;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/19 11:27:52
 * @description: CleanFlowPath服务接口
 */
public interface ICleanFlowPathService extends Service<CleanFlowPath> {


    List<CleanFlowPathVo> list(String batchNo);

    void updateStatus(Long id, String status);

    CleanFlowPath findByParam(String batchNo, String projectCode);

    void findAndUpdate(String batchNo, String projectCode, String status);

    /**
     * 清算操作
     *
     * @param id
     */
    ApiResult clean(Long id);
}
