package com.baibei.hengjia.admin.modules.advisory.service.impl;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationListVo;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationVo;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryNavigationMapper;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigation;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryNavigationService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;


/**
* @author: wenq
* @date: 2019/09/12 14:45:44
* @description: AdvisoryNavigation服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryNavigationServiceImpl extends AbstractService<AdvisoryNavigation> implements IAdvisoryNavigationService {

    @Autowired
    private AdvisoryNavigationMapper advisoryNavigationMapper;

    @Override
    public ApiResult addObj(AdvisoryNavigationDto advisoryNavigationDto) {
        AdvisoryNavigation advisoryNavigation = new AdvisoryNavigation();
        advisoryNavigation.setNavigationSort(Integer.valueOf(advisoryNavigationDto.getNavigationSort()));
        advisoryNavigation.setNavigationDisplay(advisoryNavigationDto.getNavigationDisplay());
        advisoryNavigation.setFlag((byte)1);

        if(StringUtils.isEmpty(advisoryNavigationDto.getId())){
            advisoryNavigation.setCreateTime(new Date());
            if(compareNavigationName(advisoryNavigationDto.getNavigationName())){
                return ApiResult.error("导航名已经存在");
            }
            advisoryNavigation.setNavigationName(advisoryNavigationDto.getNavigationName());
            if("show".equalsIgnoreCase(advisoryNavigationDto.getNavigationDisplay())){
                if(isDisplayTen()){
                    return ApiResult.error("导航最多允许10个显示");
                }
            }
            if(advisoryNavigationMapper.insert(advisoryNavigation) > 0){
                    return ApiResult.success();
            }
        } else {
            advisoryNavigation.setId(Long.valueOf(advisoryNavigationDto.getId()));
            advisoryNavigation.setModifyTime(new Date());

            AdvisoryNavigation oldAdvisoryNavigation = advisoryNavigationMapper.selectByPrimaryKey(advisoryNavigation);

            if(!advisoryNavigationDto.getNavigationDisplay().equals(oldAdvisoryNavigation.getNavigationDisplay())){
                if("show".equals(advisoryNavigationDto.getNavigationDisplay()) && isDisplayFiveE()){
                    return ApiResult.error("导航最多允许10个显示");
                }
            }
            if(advisoryNavigationMapper.updateByPrimaryKeySelective(advisoryNavigation) > 0){
                return ApiResult.success();
            }
        }
        return ApiResult.error();
    }

    @Override
    public MyPageInfo<AdvisoryNavigationVo> objList(AdvisoryNavigationListDto advisoryNavigationListDto) {
        PageHelper.startPage(advisoryNavigationListDto.getCurrentPage(), advisoryNavigationListDto.getPageSize());
        if(advisoryNavigationListDto.getCreateTime() != null && !"".equals(advisoryNavigationListDto.getCreateTime())){
            advisoryNavigationListDto.setCreateTime(advisoryNavigationListDto.getCreateTime()+ " 00:00:00");
        }
        if(advisoryNavigationListDto.getModifyTime() != null && !"".equals(advisoryNavigationListDto.getModifyTime())){
            advisoryNavigationListDto.setModifyTime(advisoryNavigationListDto.getModifyTime()+ " 00:00:00");
        }
        List<AdvisoryNavigationVo> advisoryNavigationVoList = advisoryNavigationMapper.objList(advisoryNavigationListDto);
        MyPageInfo<AdvisoryNavigationVo> pageInfo = new MyPageInfo<>(advisoryNavigationVoList);
        return pageInfo;
    }

    @Override
    public ApiResult batchDelete(String id) {
        AdvisoryNavigation advisoryNavigation = new AdvisoryNavigation();
        advisoryNavigation.setId(Long.valueOf(id));
        if(advisoryNavigationMapper.deleteByPrimaryKey(advisoryNavigation) > 0 ){
            return ApiResult.success();
        }
        return ApiResult.error();
    }

    @Override
    public int queryByNavigationId(String navigationId) {
      return advisoryNavigationMapper.queryByNavigationId(Long.parseLong(navigationId));
    }

    @Override
    public List<AdvisoryNavigationListVo> navigationList() {
        List<AdvisoryNavigationListVo> list = Lists.newArrayList();
        List<AdvisoryNavigation> advisoryNavigationList = advisoryNavigationMapper.selectAll();
        if(CollectionUtils.isEmpty(advisoryNavigationList)){
            return list;
        }
        for(AdvisoryNavigation advisoryNavigation : advisoryNavigationList){
            AdvisoryNavigationListVo advisoryNavigationListVo = new AdvisoryNavigationListVo();
            advisoryNavigationListVo.setId(String.valueOf(advisoryNavigation.getId()));
            advisoryNavigationListVo.setNavigationName(advisoryNavigation.getNavigationName());
            list.add(advisoryNavigationListVo);
        }
        return list;
    }

    @Override
    public boolean compareNavigationName(String navigationName){
        if(advisoryNavigationMapper.compareNavigationName(navigationName) > 0){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean isDisplayTen(){
        if(advisoryNavigationMapper.isDisplayTen() >= 10){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isDisplayFiveE(){
        if(advisoryNavigationMapper.isDisplayTen() == 10){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
