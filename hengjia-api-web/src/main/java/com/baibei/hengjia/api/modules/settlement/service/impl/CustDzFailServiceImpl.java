package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.CustDzFailMapper;
import com.baibei.hengjia.api.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.api.modules.settlement.model.CustDzFail;
import com.baibei.hengjia.api.modules.settlement.service.ICustDzFailService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/27 16:10:54
 * @description: CustDzFail服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CustDzFailServiceImpl extends AbstractService<CustDzFail> implements ICustDzFailService {

    @Autowired
    private CustDzFailMapper tblSetCustDzFailMapper;

    @Override
    public List<CustDzFail> findByBatchNo(String batchNo) {
        if (StringUtils.isEmpty(batchNo)) {
            throw new IllegalArgumentException("batchNo can not be null");
        }
        Condition condition = new Condition(CustDzFail.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
        andEqualTo("flag", Constants.Flag.VALID);
        return findByCondition(condition);
    }
}
