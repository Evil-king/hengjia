package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.model.CleanData;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/26 18:54:38
 * @description: CleanData服务接口
 */
public interface ICleanDataService extends Service<CleanData> {

    List<CleanData> findByBatchNo(String batchNo);

    /**
     * 查询指定批次号清算成功的客户编号列表
     *
     * @param batchNo
     * @return
     */
    List<String> findCleanSuccessCustomerNo(String batchNo);

    /**
     * 查询指定客户是否清算成功
     *
     * @param batchNo
     * @param customerNo
     * @return
     */
    boolean isCleanSuccess(String batchNo, String customerNo);

    /**
     * 获取指定用户是否有清算成功的记录
     *
     * @param customerNo
     * @return
     */
    Boolean isCleanSuccess(String customerNo);

    /**
     * 更新清算失败
     *
     * @param batchNo
     * @param failCustomerNoList
     */
    void updateCleanFail(String batchNo, List<String> failCustomerNoList);

    /**
     * 更新清算成功
     *
     * @param batchNo
     * @param failCustomerNoList
     */
    void updateCleanSuccess(String batchNo, List<String> failCustomerNoList);

    /**
     * 软删除
     *
     * @param idList
     */
    void softDelete(List<Long> idList);

}
