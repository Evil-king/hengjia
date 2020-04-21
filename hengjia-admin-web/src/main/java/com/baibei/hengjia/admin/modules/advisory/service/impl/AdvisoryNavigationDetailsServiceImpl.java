package com.baibei.hengjia.admin.modules.advisory.service.impl;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.EditSortDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationDetailVo;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryNavigationDetailsMapper;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryArticleService;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationDetailsService;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryVideoService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: wenq
* @date: 2019/09/14 09:47:23
* @description: AdvisoryNavigationDetails服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryNavigationDetailsServiceImpl extends AbstractService<AdvisoryNavigationDetails> implements IAdvisoryNavigationDetailsService {

    @Autowired
    private AdvisoryNavigationDetailsMapper advisoryNavigationDetailsMapper;
    @Autowired
    private IAdvisoryVideoService advisoryVideoService;
    @Autowired
    private IAdvisoryArticleService advisoryArticleService;

    @Override
    public MyPageInfo<AdvisoryNavigationDetailVo> objList(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto) {
        PageHelper.startPage(advisoryNavigationDetailsDto.getCurrentPage(), advisoryNavigationDetailsDto.getPageSize());
        if(advisoryNavigationDetailsDto.getCreateTime() != null && !"".equals(advisoryNavigationDetailsDto.getCreateTime())){
            advisoryNavigationDetailsDto.setCreateTime(advisoryNavigationDetailsDto.getCreateTime()+ " 00:00:00");
        }
        if(advisoryNavigationDetailsDto.getModifyTime() != null && !"".equals(advisoryNavigationDetailsDto.getModifyTime())){
            advisoryNavigationDetailsDto.setModifyTime(advisoryNavigationDetailsDto.getModifyTime()+ " 00:00:00");
        }
        List<AdvisoryNavigationDetailVo> advisoryNavigationVoList = advisoryNavigationDetailsMapper.objList(advisoryNavigationDetailsDto);
        MyPageInfo<AdvisoryNavigationDetailVo> pageInfo = new MyPageInfo<>(advisoryNavigationVoList);
        return pageInfo;
    }

    @Override
    public int editSort(EditSortDto editSortDto) {
        AdvisoryNavigationDetails advisoryNavigationDetails = new AdvisoryNavigationDetails();
        advisoryNavigationDetails.setId(Long.valueOf(editSortDto.getId()));
        advisoryNavigationDetails.setSort(Integer.valueOf(editSortDto.getSort()));
        advisoryNavigationDetails.setModifyTime(new Date());
        return advisoryNavigationDetailsMapper.updateByParams(advisoryNavigationDetails);
    }

    @Override
    public int deleteDetails(String id) {
        Condition condition = new Condition(AdvisoryNavigationDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", id);
        List<AdvisoryNavigationDetails> navigationDetailsList = advisoryNavigationDetailsMapper.selectByCondition(condition);
        AdvisoryNavigationDetails advisoryNavigationDetails = null;
        if(!CollectionUtils.isEmpty(navigationDetailsList)){
            advisoryNavigationDetails = navigationDetailsList.get(0);
        }
        if(advisoryNavigationDetailsMapper.deleteByPrimaryKey(advisoryNavigationDetails) > 0){
            //取消对应的关联关系
            if("video".equalsIgnoreCase(advisoryNavigationDetails.getType()) || "audio".equalsIgnoreCase(advisoryNavigationDetails.getType())){
                if(advisoryVideoService.deleteRelationship(String.valueOf(advisoryNavigationDetails.getTypeId()),
                        String.valueOf(advisoryNavigationDetails.getNavigationId()),advisoryNavigationDetails.getType()) > 0){
                    return 1;
                }
            }
            if("article".equalsIgnoreCase(advisoryNavigationDetails.getType())){
                if(advisoryArticleService.deleteRelationship(String.valueOf(advisoryNavigationDetails.getTypeId()),
                        String.valueOf(advisoryNavigationDetails.getNavigationId())) > 0){
                    return 1;
                }
            }

        }
        return 0;
    }

    @Override
    public MyPageInfo<AdvisoryNavigationDetailVo> objListByNavigationId(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto) {
        PageHelper.startPage(advisoryNavigationDetailsDto.getCurrentPage(), advisoryNavigationDetailsDto.getPageSize());
        List<AdvisoryNavigationDetailVo> advisoryNavigationVoList = advisoryNavigationDetailsMapper.objListByNavigationId(advisoryNavigationDetailsDto);
        MyPageInfo<AdvisoryNavigationDetailVo> pageInfo = new MyPageInfo<>(advisoryNavigationVoList);
        return pageInfo;
    }
}
