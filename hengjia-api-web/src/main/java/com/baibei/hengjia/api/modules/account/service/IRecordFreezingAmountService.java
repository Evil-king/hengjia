package com.baibei.hengjia.api.modules.account.service;

import com.baibei.hengjia.api.modules.account.model.RecordFreezingAmount;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerFrozenVo;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;
import java.util.Map;


/**
 * @author: hyc
 * @date: 2019/06/03 14:41:29
 * @description: RecordFreezingAmount服务接口
 */
public interface IRecordFreezingAmountService extends Service<RecordFreezingAmount> {

    /**
     * 统计客户在时间段内的冻结与解冻总和
     *
     * @param params
     * @return
     */
    List<CustomerFrozenVo> sumCustomerFrozenList(Map<String, Object> params);

}
