package com.baibei.hengjia.api.modules.advisory.service.impl;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationDetailsVo;
import com.baibei.hengjia.api.modules.advisory.dao.AdvisoryNavigationDetailsMapper;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryArticleService;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryNavigationDetailsService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private IAdvisoryArticleService advisoryArticleService;

    @Override
    public MyPageInfo<AdvisoryNavigationDetailsVo> navigationDetails(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto) {
        PageHelper.startPage(advisoryNavigationDetailsDto.getCurrentPage(), advisoryNavigationDetailsDto.getPageSize());
        List<AdvisoryNavigationDetailsVo> pageList = advisoryNavigationDetailsMapper.navigationDetails(advisoryNavigationDetailsDto);
        for(int i = 0;i<pageList.size();i++){
            if("article".equals(pageList.get(i).getType())){
                AdvisoryNavigationDetailsVo advisoryNavigationDetailsVo =  pageList.get(i);
                AdvisoryArticle advisoryArticle = advisoryArticleService.selectByParams(String.valueOf(advisoryNavigationDetailsVo.getTypeId()));
                if(advisoryArticle != null){
                    advisoryNavigationDetailsVo.setUrl(advisoryArticle.getArticleUrl());
                }
            }
        }
        MyPageInfo<AdvisoryNavigationDetailsVo> pageInfo = new MyPageInfo<>(pageList);
        return pageInfo;
    }
}
