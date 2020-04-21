package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.CleanProgress;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
 * @author: uqing
 * @date: 2019/06/28 19:47:18
 * @description: CleanProgress服务接口
 */
public interface ICleanProgressService extends Service<CleanProgress> {

    /**
     * 查看清算进度
     *
     * @param batchNo
     * @return
     */
    CleanProgress findByBatchNo(String batchNo);

    /**
     * 查询指定批次号是否完成清算
     *
     * @param batchNo
     * @return
     */
    boolean isCompletedClean(String batchNo);


    /**
     * 查询当天是否完成清算
     *
     * @return
     */
    boolean isCompletedClean();


}
