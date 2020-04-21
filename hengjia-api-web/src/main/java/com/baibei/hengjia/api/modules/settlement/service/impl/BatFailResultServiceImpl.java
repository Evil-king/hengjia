package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.BatFailResultMapper;
import com.baibei.hengjia.api.modules.settlement.model.BatFailResult;
import com.baibei.hengjia.api.modules.settlement.service.IBatFailResultService;
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
 * @date: 2019/06/27 17:08:02
 * @description: BatFailResult服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class BatFailResultServiceImpl extends AbstractService<BatFailResult> implements IBatFailResultService {

    @Autowired
    private BatFailResultMapper tblSetBatFailResultMapper;

    @Override
    public List<BatFailResult> findByBatchNo(String batchNo) {
        if (StringUtils.isEmpty(batchNo)) {
            throw new IllegalArgumentException("batchNo can not be null");
        }
        Condition condition = new Condition(BatFailResult.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("flag", Constants.Flag.VALID);
        return findByCondition(condition);
    }
}
