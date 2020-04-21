package com.baibei.hengjia.api.modules.settlement.service;

import com.baibei.hengjia.api.modules.settlement.bean.dto.DealDiffDto;
import com.baibei.hengjia.api.modules.settlement.model.WithDrawDepositDiff;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

/**
 * @Classname IWithDrawDepositDiffService
 * @Description 出入金对账
 * @Date 2019/6/25 11:34
 * @Created by Longer
 */
public interface IWithDrawDepositDiffService extends Service<WithDrawDepositDiff> {

    /**
     * 出入金对账
     * @param batchNo 批次号
     * @return
     */
    ApiResult withDrawDepositDiff(String batchNo);


    /**
     * 出入金对账处理服务
     * @param dealDiffDto
     * @return
     */
    ApiResult dealDiff(DealDiffDto dealDiffDto);

    int deleteByBatchNo(String batchNo);

    /**
     * 更新，乐观锁处理（只能更新 状态为 wait 的记录）
     * @param withDrawDepositDiff
     * @return
     */
    int updateDiffWithWait(WithDrawDepositDiff withDrawDepositDiff);
}
