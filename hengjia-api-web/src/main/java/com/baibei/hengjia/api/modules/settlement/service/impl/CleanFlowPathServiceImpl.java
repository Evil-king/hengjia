package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.CleanFlowPathMapper;
import com.baibei.hengjia.api.modules.settlement.model.CleanFlowPath;
import com.baibei.hengjia.api.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/07/19 11:27:52
 * @description: CleanFlowPath服务实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class CleanFlowPathServiceImpl extends AbstractService<CleanFlowPath> implements ICleanFlowPathService {

    @Autowired
    private CleanFlowPathMapper tblSetCleanFlowPathMapper;

    @Override
    public void updateStatus(Long id, String status) {
        if (id == null || StringUtils.isEmpty(status)) {
            throw new IllegalArgumentException("参数有误");
        }
        Condition condition = new Condition(CleanFlowPath.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("id", id);
        CleanFlowPath cleanFlowPath = new CleanFlowPath();
        cleanFlowPath.setModifyTime(new Date());
        cleanFlowPath.setStatus(status);
        cleanFlowPath.setExecuteTime(new Date());
        this.updateByConditionSelective(cleanFlowPath, condition);
    }

    @Override
    public CleanFlowPath findByParam(String batchNo, String projectCode) {
        Condition condition = new Condition(CleanFlowPath.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("batchNo", batchNo);
        criteria.andEqualTo("projectCode", projectCode);
        return findOneByCondition(condition);
    }

    @Override
    public void findAndUpdate(String batchNo, String projectCode, String status) {
        CleanFlowPath cleanFlowPath = findByParam(batchNo, projectCode);
        if (cleanFlowPath == null) {
            log.error("清算进度记录不存在,batchNo={},projectCode={}");
            return;
        }
        updateStatus(cleanFlowPath.getId(), status);
    }
}
