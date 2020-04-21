package com.baibei.hengjia.admin.modules.settlement.service.impl;

import com.baibei.hengjia.admin.feign.ApiFeign;
import com.baibei.hengjia.admin.modules.settlement.bean.vo.CleanFlowPathVo;
import com.baibei.hengjia.admin.modules.settlement.dao.CleanFlowPathMapper;
import com.baibei.hengjia.admin.modules.settlement.enums.CleanFlowPathStatusEnum;
import com.baibei.hengjia.admin.modules.settlement.model.CleanFlowPath;
import com.baibei.hengjia.admin.modules.settlement.service.ICleanFlowPathService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import com.baibei.hengjia.common.tool.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
    @Autowired
    private ApiFeign apiFeign;

    @Override
    public List<CleanFlowPathVo> list(String batchNo) {
        Condition condition = new Condition(CleanFlowPath.class);
        condition.setOrderByClause("sequence asc");
        Example.Criteria criteria = buildValidCriteria(condition);
        if (StringUtils.isEmpty(batchNo)) {
            batchNo = DateUtil.yyyyMMddNoLine.get().format(new Date());
        }
        criteria.andEqualTo("batchNo", batchNo);
        List<CleanFlowPath> list = findByCondition(condition);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<CleanFlowPathVo> result = BeanUtil.copyProperties(list, CleanFlowPathVo.class);
        for (CleanFlowPathVo vo : result) {
            vo.setStatus(CleanFlowPathStatusEnum.getMsg(vo.getStatus()));
        }
        return result;
    }

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
            throw new IllegalArgumentException("cleanFlowPath不存在");
        }
        updateStatus(cleanFlowPath.getId(), status);
    }

    @Override
    public ApiResult clean(Long id) {
        CleanFlowPath cleanFlowPath = findById(id);
        if (cleanFlowPath == null) {
            return ApiResult.badParam("清算操作记录不存在");
        }
        if (Constants.CleanFlowPathStatus.COMPLETED.equals(cleanFlowPath.getStatus())) {
            return ApiResult.badParam("该操作已完成");
        }
        String batchNo = cleanFlowPath.getBatchNo();
        String projectCode = cleanFlowPath.getProjectCode();
        if (Constants.CleanFlowPathCode.SIGN_BACK.equals(projectCode)) {
            return apiFeign.signBack(batchNo);
        } else if (Constants.CleanFlowPathCode.ACCOUNTCHECK.equals(projectCode)) {
            return apiFeign.accountcheck(batchNo);
        } else if (Constants.CleanFlowPathCode.AMOUNT_RETURN.equals(projectCode)) {
            return apiFeign.amountReturn(batchNo);
        } else if (Constants.CleanFlowPathCode.CLEAN_FILE.equals(projectCode)) {
            return apiFeign.generateCleanData(batchNo);
        } else if (Constants.CleanFlowPathCode.LAUNCH_CLEAN.equals(projectCode)) {
            return apiFeign.launchClean(batchNo);
        } else if (Constants.CleanFlowPathCode.CLEAN_PROCESS.equals(projectCode)) {
            return apiFeign.cleanProcess(batchNo);
        } else {
            return ApiResult.badParam("未知的操作类型");
        }
    }
}
