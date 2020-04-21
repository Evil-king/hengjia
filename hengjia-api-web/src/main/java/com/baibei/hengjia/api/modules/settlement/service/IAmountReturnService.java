package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.AmountReturn;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/02 16:49:07
 * @description: AmountReturn服务接口
 */
public interface IAmountReturnService extends Service<AmountReturn> {

    List<AmountReturn> findByBatchNo(String batchNo, String type);

    /**
     * 业务处理
     *
     * @param batchNo
     * @param type
     */
    ApiResult process(String batchNo, String type);

    /**
     * 业务办理
     *
     * @param id
     * @return
     */
    ApiResult processById(String id);

}
