package com.baibei.hengjia.api.modules.match.service.impl;

import com.baibei.hengjia.api.modules.match.bean.dto.MatchPlanDetailDto;
import com.baibei.hengjia.api.modules.match.dao.MatchPlanDetailMapper;
import com.baibei.hengjia.api.modules.match.model.MatchPlanDetail;
import com.baibei.hengjia.api.modules.match.service.IMatchPlanDetailService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: Longer
* @date: 2019/08/07 17:57:17
* @description: MatchPlanDetail服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MatchPlanDetailServiceImpl extends AbstractService<MatchPlanDetail> implements IMatchPlanDetailService {

    @Autowired
    private MatchPlanDetailMapper tblCpMatchPlanDetailMapper;

    @Override
    public ApiResult insertList(MatchPlanDetailDto matchPlanDetailDto, Long id) {
            MatchPlanDetail matchPlanDetail=BeanUtil.copyProperties(matchPlanDetailDto,MatchPlanDetail.class);
            matchPlanDetail.setPlanId(id);
            matchPlanDetail.setModifyTime(new Date());
            matchPlanDetail.setFlag(new Byte("1"));
            matchPlanDetail.setStatus(Constants.MatchFailLogStatus.WAIT);
            try {
                tblCpMatchPlanDetailMapper.insertSelective(matchPlanDetail);
                return  ApiResult.success();
            }catch (DuplicateKeyException e){
                log.info("配券明细插入失败，流水号"+matchPlanDetailDto.getPlanNo()+"已存在");
                return ApiResult.error();
            }
    }

    @Override
    public List<MatchPlanDetail> findbyPlanId(Long id, String status) {
        return tblCpMatchPlanDetailMapper.findbyPlanId(id,status);
    }

    @Override
    public List<MatchPlanDetail> findByCustomerNoAndPlanId(String customerNo, Long id) {
        Condition condition=new Condition(MatchPlanDetail.class);
        Example.Criteria criteria=buildValidCriteria(condition);
        criteria.andEqualTo("customerNo",customerNo);
        criteria.andEqualTo("planId",id);
        return tblCpMatchPlanDetailMapper.selectByCondition(condition);
    }
}
