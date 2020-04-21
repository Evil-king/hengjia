package com.baibei.hengjia.admin.modules.advisory.service.impl;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryNavigationDetailsMapper;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryVideoMapper;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryVideo;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryVideoService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.github.pagehelper.PageHelper;
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
* @description: AdvisoryVideo服务实现
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryVideoServiceImpl extends AbstractService<AdvisoryVideo> implements IAdvisoryVideoService {

    @Autowired
    private AdvisoryVideoMapper advisoryVideoMapper;
    @Autowired
    private AdvisoryNavigationDetailsMapper advisoryNavigationDetailsMapper;

    @Override
    public MyPageInfo<AdvisoryVideoVo> objList(AdvisoryVideoListDto advisoryVideoListDto) {
        PageHelper.startPage(advisoryVideoListDto.getCurrentPage(), advisoryVideoListDto.getPageSize());
        if(advisoryVideoListDto.getCreateTime() != null && !"".equals(advisoryVideoListDto.getCreateTime())){
            advisoryVideoListDto.setCreateTime(advisoryVideoListDto.getCreateTime()+ " 00:00:00");
        }
        if(advisoryVideoListDto.getModifyTime() != null && !"".equals(advisoryVideoListDto.getModifyTime())){
            advisoryVideoListDto.setModifyTime(advisoryVideoListDto.getModifyTime()+ " 00:00:00");
        }
        List<AdvisoryVideoVo> advisoryVedioVoList = advisoryVideoMapper.objList(advisoryVideoListDto);
        MyPageInfo<AdvisoryVideoVo> pageInfo = new MyPageInfo<>(advisoryVedioVoList);
        return pageInfo;
    }

    @Override
    public ApiResult addAndEdit(AdvisoryVideoDto advisoryVideoDto) {
        AdvisoryVideo advisoryVideo = new AdvisoryVideo();
        advisoryVideo.setVideoType(advisoryVideoDto.getVideoType());
        advisoryVideo.setVideoTitle(advisoryVideoDto.getVideoTitle());
        if(advisoryVideoDto.getVideoSort() == null || advisoryVideoDto.getVideoSort() == ""){
            advisoryVideoDto.setVideoSort(null);
        } else {
            advisoryVideo.setVideoSort(Integer.valueOf(advisoryVideoDto.getVideoSort()));
        }
        advisoryVideo.setVideoIndex(advisoryVideoDto.getVideoIndex());
        advisoryVideo.setVideoUrl(advisoryVideoDto.getVideoUrl());
        advisoryVideo.setVideoDisplay(advisoryVideoDto.getVideoDisplay());
        advisoryVideo.setNavigationId(String.valueOf(advisoryVideoDto.getNavigationId()));
        advisoryVideo.setFlag((byte)1);

        String str[] = advisoryVideoDto.getNavigationId().split(",");

        if(StringUtils.isEmpty(advisoryVideoDto.getId())){
            advisoryVideo.setCreateTime(new Date());
            if(advisoryVideoMapper.insert(advisoryVideo) > 0){
                for (int i = 0; i < str.length; i++){
                    AdvisoryNavigationDetails advisoryNavigationDetails = operatorObj(advisoryVideo);
                    advisoryNavigationDetails.setNavigationId(Long.valueOf(str[i]));
                    advisoryNavigationDetails.setFlag((byte)1);
                    advisoryNavigationDetails.setCreateTime(advisoryVideo.getCreateTime());
                    advisoryNavigationDetails.setModifyTime(new Date());
                    advisoryNavigationDetailsMapper.insert(advisoryNavigationDetails);
                }
                return ApiResult.success();
            }
        } else {
            advisoryVideo.setId(Long.valueOf(advisoryVideoDto.getId()));
            advisoryVideo.setModifyTime(new Date());
            if (advisoryVideoMapper.updateByParams(advisoryVideo) > 0) {

                Condition condition = new Condition(AdvisoryNavigationDetails.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("typeId",String.valueOf(advisoryVideo.getId()));
                if("video".equals(advisoryVideoDto.getVideoType())){
                    criteria.andEqualTo("type",advisoryVideoDto.getVideoType());
                }
                if("audio".equals(advisoryVideoDto.getVideoType())){
                    criteria.andEqualTo("type",advisoryVideoDto.getVideoType());
                }
                advisoryNavigationDetailsMapper.deleteByCondition(condition);

                for (int i = 0; i < str.length; i++){
                    AdvisoryNavigationDetails advisoryNavigationDetails = operatorObj(advisoryVideo);
                    advisoryNavigationDetails.setCreateTime(new Date());
                    advisoryNavigationDetails.setModifyTime(advisoryVideo.getModifyTime());
                    advisoryNavigationDetails.setNavigationId(Long.valueOf(str[i]));
                    advisoryNavigationDetailsMapper.insert(advisoryNavigationDetails);
                }
            }
            return ApiResult.success();
        }
        return null;
    }

    @Override
    public AdvisoryVideoVo lookObj(long id) {
        Condition condition = new Condition(AdvisoryVideo.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", id);
        List<AdvisoryVideo> advisoryVideoList = advisoryVideoMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(advisoryVideoList)) {
            return new AdvisoryVideoVo();
        }
        AdvisoryVideoVo advisoryVideoVo = new AdvisoryVideoVo();
        AdvisoryVideo advisoryVideo = advisoryVideoList.get(0);
        if(advisoryVideo.getVideoSort() == null){
            advisoryVideo.setVideoSort(0);
            advisoryVideoVo.setVideoSort("");
        } else {
            advisoryVideoVo.setVideoSort(String.valueOf(advisoryVideo.getVideoSort()));
        }
        advisoryVideoVo.setId(advisoryVideo.getId());
        advisoryVideoVo.setCreateTime(String.valueOf(advisoryVideo.getCreateTime()));
        advisoryVideoVo.setModifyTime(String.valueOf(advisoryVideo.getModifyTime()));
        advisoryVideoVo.setNavigationId(advisoryVideo.getNavigationId());
        advisoryVideoVo.setVideoDisplay(advisoryVideo.getVideoDisplay());
        advisoryVideoVo.setVideoIndex(advisoryVideo.getVideoIndex());
        advisoryVideoVo.setVideoTitle(advisoryVideo.getVideoTitle());
        advisoryVideoVo.setVideoType(advisoryVideo.getVideoType());
        advisoryVideoVo.setVideoUrl(advisoryVideo.getVideoUrl());
        return advisoryVideoVo;
    }

    @Override
    public ApiResult batchOperator(String id, String type) {

        AdvisoryVideo advisoryVideo = null;
        Condition condition1 = new Condition(AdvisoryVideo.class);
        Example.Criteria criteria1 = condition1.createCriteria();
        criteria1.andEqualTo("id", id);
        List<AdvisoryVideo> advisoryVideos = advisoryVideoMapper.selectByCondition(condition1);
        if(!CollectionUtils.isEmpty(advisoryVideos)){
            advisoryVideo = advisoryVideos.get(0);
        }
        AdvisoryNavigationDetails advisoryNavigationDetails = new AdvisoryNavigationDetails();
        Condition condition = new Condition(AdvisoryNavigationDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("typeId",id);
        if("video".equalsIgnoreCase(advisoryVideo.getVideoType())){
            criteria.andEqualTo("type", advisoryVideo.getVideoType());
        }
        if("audio".equalsIgnoreCase(advisoryVideo.getVideoType())){
            criteria.andEqualTo("type", advisoryVideo.getVideoType());
        }
        advisoryVideo.setModifyTime(new Date()) ;
        advisoryVideo.setVideoType(advisoryVideo.getVideoType());
        if ("delete".equals(type)) {
            if(advisoryVideoMapper.deleteByPrimaryKey(advisoryVideo) > 0 && advisoryNavigationDetailsMapper.deleteByCondition(condition)>0){
                return ApiResult.success();
            }
            return ApiResult.error();
        }
        if ("show".equals(type)) {
            advisoryVideo.setVideoDisplay(type);
        }
        if ("hidden".equals(type)) {
            advisoryVideo.setVideoDisplay(type);
        }
        if (advisoryVideoMapper.updateByPrimaryKeySelective(advisoryVideo) < 0) {
            return ApiResult.error();
        }
        return ApiResult.success();
    }

    @Override
    public int deleteRelationship(String typeId, String navigationId,String type) {
        Condition condition = new Condition(AdvisoryVideo.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", typeId);
        criteria.andEqualTo("videoType", type);
        List<AdvisoryVideo> advisoryVideos = advisoryVideoMapper.selectByCondition(condition);
        if(!CollectionUtils.isEmpty(advisoryVideos)){
            AdvisoryVideo advisoryVideo = advisoryVideos.get(0);
            if(advisoryVideo.getNavigationId().contains(navigationId)){
                String strArry[] = advisoryVideo.getNavigationId().split(",");
                if(!strArry[0].equals(navigationId)){
                    advisoryVideo.setNavigationId(advisoryVideo.getNavigationId().replace(","+navigationId,""));
                } else {
                    advisoryVideo.setNavigationId(advisoryVideo.getNavigationId().replace(navigationId+",",""));
                }
            }
            advisoryVideo.setModifyTime(new Date());
            if(advisoryVideoMapper.updateByPrimaryKeySelective(advisoryVideo) > 0){
                return 1;
            }
        }
        return 0;
    }

    private AdvisoryNavigationDetails operatorObj(AdvisoryVideo advisoryVideo){
        AdvisoryNavigationDetails advisoryNavigationDetails =new AdvisoryNavigationDetails();
        advisoryNavigationDetails.setFlag((byte) 1);
        advisoryNavigationDetails.setTypeId(advisoryVideo.getId());
        advisoryNavigationDetails.setTitle(advisoryVideo.getVideoTitle());
        advisoryNavigationDetails.setImage(advisoryVideo.getVideoIndex());
        advisoryNavigationDetails.setUrl(advisoryVideo.getVideoUrl());
        advisoryNavigationDetails.setType(advisoryVideo.getVideoType());
        return advisoryNavigationDetails;
    }

    private Condition operatorCondition(String navigationId,String id){
        Condition condition = new Condition(AdvisoryNavigationDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("navigationId",navigationId);
        criteria.andEqualTo("typeId",id);
        return condition;
    }
}
