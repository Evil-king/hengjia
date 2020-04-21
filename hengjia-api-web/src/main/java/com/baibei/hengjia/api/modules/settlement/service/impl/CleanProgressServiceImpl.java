package com.baibei.hengjia.api.modules.settlement.service.impl;

import com.baibei.hengjia.api.modules.settlement.dao.CleanProgressMapper;
import com.baibei.hengjia.api.modules.settlement.model.CleanProgress;
import com.baibei.hengjia.api.modules.settlement.service.ICleanProgressService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
 * @author: uqing
 * @date: 2019/06/28 19:47:18
 * @description: CleanProgress服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CleanProgressServiceImpl extends AbstractService<CleanProgress> implements ICleanProgressService {

    @Autowired
    private CleanProgressMapper tblSetCleanProgressMapper;

    @Override
    public CleanProgress findByBatchNo(String batchNo) {
        if (StringUtils.isEmpty(batchNo)) {
            throw new IllegalArgumentException("batchNo can not be null");
        }
        Condition condition = new Condition(CleanProgress.class);
        Example.Criteria criteria = buildValidCriteria(condition);
        criteria.andEqualTo("batchNo", batchNo);
        List<CleanProgress> list = findByCondition(condition);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public boolean isCompletedClean(String batchNo) {
        CleanProgress cleanProgress = findByBatchNo(batchNo);
        return (cleanProgress != null && (Constants.CleanProcessResult.ALL_SUCCESS.equals(cleanProgress.getResultFlag())
        || Constants.CleanProcessResult.SOME_SUCCESS.equals(cleanProgress.getResultFlag())));
    }

    @Override
    public boolean isCompletedClean() {
        String batchNo = DateUtil.yyyyMMddNoLine.get().format(new Date());
        return isCompletedClean(batchNo);
    }
}
