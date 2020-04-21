package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.dao.MatchFailLogMapper;
import com.baibei.hengjia.api.modules.match.model.MatchFailLog;
import com.baibei.hengjia.api.modules.match.service.IMatchFailLogService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/08/05 11:08:53
* @description: MatchFailLog服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class MatchFailLogServiceImpl extends AbstractService<MatchFailLog> implements IMatchFailLogService {

    @Autowired
    private MatchFailLogMapper matchFailLogMapper;

    @Override
    public List<MatchFailLog> getFailLogsByStatus(String status) {
        Condition condition = new Condition(MatchFailLog.class);
        condition.setOrderByClause("create_time asc,id");
        Example.Criteria criteria = condition.createCriteria();
        if (!StringUtils.isEmpty(status)) {
            criteria.andEqualTo("status",status);
        }
        List<MatchFailLog> matchFailLogs = matchFailLogMapper.selectByCondition(condition);
        return matchFailLogs;
    }

    @Override
    public List<MatchFailLog> getCurrentDayFailLogs(String status) {
        return matchFailLogMapper.selectCurrentDayFailLogs(status);
    }

    @Override
    public int modifyFailLog(MatchFailLog matchFailLog) {
        int i = matchFailLogMapper.update(matchFailLog);
        return i;
    }

    @Override
    public void destroyFailLog(String status) {
        Condition condition = new Condition(MatchFailLog.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("status",status);
        MatchFailLog matchFailLog = new MatchFailLog();
        matchFailLog.setModifyTime(new Date());
        matchFailLog.setStatus(Constants.MatchFailLogStatus.DESTORY);
        matchFailLogMapper.updateByConditionSelective(matchFailLog,condition);
    }
}
