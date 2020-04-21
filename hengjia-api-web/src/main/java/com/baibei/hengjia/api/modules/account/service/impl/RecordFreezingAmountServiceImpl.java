package com.baibei.hengjia.api.modules.account.service.impl;

import com.baibei.hengjia.api.modules.account.dao.RecordFreezingAmountMapper;
import com.baibei.hengjia.api.modules.account.model.RecordFreezingAmount;
import com.baibei.hengjia.api.modules.account.service.IRecordFreezingAmountService;
import com.baibei.hengjia.api.modules.settlement.bean.vo.CustomerFrozenVo;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


/**
 * @author: hyc
 * @date: 2019/06/03 14:41:29
 * @description: RecordFreezingAmount服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RecordFreezingAmountServiceImpl extends AbstractService<RecordFreezingAmount> implements IRecordFreezingAmountService {

    @Autowired
    private RecordFreezingAmountMapper tblRecordFreezingAmountMapper;

    @Override
    public List<CustomerFrozenVo> sumCustomerFrozenList(Map<String, Object> params) {
        return tblRecordFreezingAmountMapper.sumCustomerFrozenList(params);
    }
}
