package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.CleanLogMapper;
import com.baibei.hengjia.api.modules.settlement.model.CleanLog;
import com.baibei.hengjia.api.modules.settlement.service.ICleanLogService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import java.util.Date;
import java.util.List;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/06/26 20:47:15
 * @description: CleanLog服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CleanLogServiceImpl extends AbstractService<CleanLog> implements ICleanLogService {

    @Autowired
    private CleanLogMapper tblSetCleanLogMapper;

    @Override
    public CleanLog findByBatchNo(String batchNo) {
        Condition condition = new Condition(CleanLog.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("flag", Constants.Flag.VALID);
        List<CleanLog> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public boolean updateByBatchNo(String batchNo, String status, String resp) {
        Condition condition = new Condition(CleanLog.class);
        condition.createCriteria().andEqualTo("batchNo", batchNo).
                andEqualTo("flag", Constants.Flag.VALID);
        CleanLog cleanLog = new CleanLog();
        cleanLog.setStatus(status);
        cleanLog.setResp(resp);
        cleanLog.setModifyTime(new Date());
        return updateByConditionSelective(cleanLog, condition);
    }
}
