package com.baibei.hengjia.admin.modules.advisory.service.impl;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryBannerMapper;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryBanner;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryBannerService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryBanner服务实现
*/
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryBannerServiceImpl extends AbstractService<AdvisoryBanner> implements IAdvisoryBannerService {

    @Autowired
    private AdvisoryBannerMapper advisoryBannerMapper;

    @Override
    public ApiResult addObject(AdvisoryBannerDto advisoryBannerDto) {
        AdvisoryBanner advisoryBanner = new AdvisoryBanner();
        advisoryBanner.setBannerTitle(advisoryBannerDto.getBannerTitle());
        advisoryBanner.setBannerContent(advisoryBannerDto.getBannerContent());
        advisoryBanner.setBannerDisplay(advisoryBannerDto.getBannerDisplay());
        advisoryBanner.setBannerImage(advisoryBannerDto.getBannerImage());
        if(advisoryBannerDto.getBannerSort() == null || advisoryBannerDto.getBannerSort() == ""){
            advisoryBanner.setBannerSort(null);
        } else {
            advisoryBanner.setBannerSort(Integer.valueOf(advisoryBannerDto.getBannerSort()));
        }
        advisoryBanner.setBannerUrl(advisoryBannerDto.getBannerUrl());
        advisoryBanner.setFlag((byte)1);

        if(StringUtils.isEmpty(advisoryBannerDto.getId())){
            if("show".equals(advisoryBannerDto.getBannerDisplay())){
                if(isDisplayFive()){
                    return ApiResult.error("banner只允许五个显示");
                }
            }
            advisoryBanner.setCreateTime(new Date());
            if(advisoryBannerMapper.insert(advisoryBanner) > 0){
                return ApiResult.success();
            }
        } else {
            advisoryBanner.setId(Long.valueOf(advisoryBannerDto.getId()));
            advisoryBanner.setModifyTime(new Date());
            AdvisoryBanner oldAdvisoryBanner = advisoryBannerMapper.selectByPrimaryKey(advisoryBanner);
            if(!advisoryBannerDto.getBannerDisplay().equals(oldAdvisoryBanner.getBannerDisplay())){
                if("show".equals(advisoryBannerDto.getBannerDisplay()) && isDisplayFiveE()){
                    return ApiResult.error("banner只允许五个显示");
                }
            }
            if(advisoryBannerMapper.updateByParams(advisoryBanner) > 0){
                return ApiResult.success();
            }
        }
        return ApiResult.error();
    }

    @Override
    public MyPageInfo<AdvisoryBannerVo> ObjList(AdvisoryBannerListDto advisoryBannerListDto) {
        PageHelper.startPage(advisoryBannerListDto.getCurrentPage(), advisoryBannerListDto.getPageSize());
        if(advisoryBannerListDto.getCreateTime() != null && !"".equals(advisoryBannerListDto.getCreateTime())){
            advisoryBannerListDto.setCreateTime(advisoryBannerListDto.getCreateTime()+ " 00:00:00");
        }
        if(advisoryBannerListDto.getModifyTime() != null && !"".equals(advisoryBannerListDto.getModifyTime())){
            advisoryBannerListDto.setModifyTime(advisoryBannerListDto.getModifyTime()+ " 00:00:00");
        }
        List<AdvisoryBannerVo> advisoryBannerVoList = advisoryBannerMapper.objList(advisoryBannerListDto);
        MyPageInfo<AdvisoryBannerVo> pageInfo = new MyPageInfo<>(advisoryBannerVoList);
        return pageInfo;
    }

    @Override
    public ApiResult batchOperator(String id,String type) {
        AdvisoryBanner advisoryBanner = new AdvisoryBanner();
        advisoryBanner.setId(Long.valueOf(id));
        advisoryBanner.setModifyTime(new Date());
        if("delete".equals(type)){
            if(advisoryBannerMapper.deleteByPrimaryKey(advisoryBanner) > 0 ){
                return ApiResult.success();
            }
            return ApiResult.error();
        }
        if("show".equals(type)){
            if(isDisplayFive()){
                return ApiResult.error("banner只允许五个显示");
            }
            advisoryBanner.setBannerDisplay(type);
        }
        if("hidden".equals(type)){
            advisoryBanner.setBannerDisplay(type);
        }
        if(advisoryBannerMapper.updateByPrimaryKeySelective(advisoryBanner) < 0){
            return ApiResult.error();
        }
        return ApiResult.success();
    }

    @Override
    public AdvisoryBannerVo lookObj(long id) {
        Condition condition = new Condition(AdvisoryBanner.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id",id);
        List<AdvisoryBanner> advisoryBannerList = advisoryBannerMapper.selectByCondition(condition);
        if(CollectionUtils.isEmpty(advisoryBannerList)){
            return new AdvisoryBannerVo();
        }
        AdvisoryBannerVo advisoryBannerVo = new AdvisoryBannerVo();
        AdvisoryBanner advisoryBanner = advisoryBannerList.get(0);
        if(advisoryBanner.getBannerSort() == null){
            advisoryBanner.setBannerSort(0);
            advisoryBannerVo.setBannerSort("");
        }else {
            advisoryBannerVo.setBannerSort(String.valueOf(advisoryBanner.getBannerSort()));
        }
        advisoryBannerVo.setId(advisoryBanner.getId());
        advisoryBannerVo.setBannerTitle(advisoryBanner.getBannerTitle());
        advisoryBannerVo.setBannerContent(advisoryBanner.getBannerContent());
        advisoryBannerVo.setBannerDisplay(advisoryBanner.getBannerDisplay());
        advisoryBannerVo.setBannerImage(advisoryBanner.getBannerImage());
        advisoryBannerVo.setBannerUrl(advisoryBanner.getBannerUrl());
        advisoryBannerVo.setCreateTime(String.valueOf(advisoryBanner.getCreateTime()));
        advisoryBannerVo.setModifyTime(String.valueOf(advisoryBanner.getModifyTime()));
        return advisoryBannerVo;
    }

    private boolean isDisplayFive(){
        if(advisoryBannerMapper.isDisplayFive() >= 5){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private boolean isDisplayFiveE(){
        if(advisoryBannerMapper.isDisplayFive() == 5){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
