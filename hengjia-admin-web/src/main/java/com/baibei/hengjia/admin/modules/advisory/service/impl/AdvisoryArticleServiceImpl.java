package com.baibei.hengjia.admin.modules.advisory.service.impl;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryArticleMapper;
import com.baibei.hengjia.admin.modules.advisory.dao.AdvisoryNavigationDetailsMapper;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.admin.modules.advisory.service.IAdvisoryArticleService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import com.baibei.hengjia.common.tool.utils.BeanUtil;
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
 * @description: AdvisoryArticle服务实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdvisoryArticleServiceImpl extends AbstractService<AdvisoryArticle> implements IAdvisoryArticleService {

    @Autowired
    private AdvisoryArticleMapper advisoryArticleMapper;
    @Autowired
    private AdvisoryNavigationDetailsMapper advisoryNavigationDetailsMapper;

    @Override
    public ApiResult addAndEdit(AdvisoryArticleDto advisoryArticleDto) {

            AdvisoryArticle advisoryArticle = new AdvisoryArticle();
            advisoryArticle.setArticleContent(advisoryArticleDto.getArticleContent());
            advisoryArticle.setArticleDisplay(advisoryArticleDto.getArticleDisplay());
            advisoryArticle.setArticleImage(advisoryArticleDto.getArticleImage());
            advisoryArticle.setArticleTitle(advisoryArticleDto.getArticleTitle());
            advisoryArticle.setArticleType(advisoryArticleDto.getArticleType());
            advisoryArticle.setArticleUrl(advisoryArticleDto.getArticleUrl());
            advisoryArticle.setNavigationId(String.valueOf(advisoryArticleDto.getNavigationId()));
            advisoryArticle.setFlag((byte) 1);

        if (StringUtils.isEmpty(advisoryArticleDto.getId())) {
            advisoryArticle.setCreateTime(new Date());
            if (advisoryArticleMapper.insertSelective(advisoryArticle) > 0) {
                String str[] = advisoryArticleDto.getNavigationId().split(",");
                for (int i = 0; i < str.length; i++){
                    AdvisoryNavigationDetails advisoryNavigationDetails = operatorObj(advisoryArticle);
                    advisoryNavigationDetails.setNavigationId(Long.valueOf(str[i]));
                    advisoryNavigationDetails.setFlag((byte)1);
                    advisoryNavigationDetails.setCreateTime(advisoryArticle.getCreateTime());
                    advisoryNavigationDetails.setModifyTime(new Date());
                    advisoryNavigationDetailsMapper.insertSelective(advisoryNavigationDetails);
                }
                return ApiResult.success();
            }
        } else {
            advisoryArticle.setId(Long.valueOf(advisoryArticleDto.getId()));
            advisoryArticle.setModifyTime(new Date());
            if (advisoryArticleMapper.updateByPrimaryKeySelective(advisoryArticle) > 0) {
                String str[] = advisoryArticleDto.getNavigationId().split(",");

                Condition condition = new Condition(AdvisoryNavigationDetails.class);
                Example.Criteria criteria = condition.createCriteria();
                criteria.andEqualTo("typeId",String.valueOf(advisoryArticle.getId()));
                criteria.andEqualTo("type", "article");
                advisoryNavigationDetailsMapper.deleteByCondition(condition);

                for (int i = 0; i < str.length; i++){
                    AdvisoryNavigationDetails advisoryNavigationDetails = operatorObj(advisoryArticle);
                    advisoryNavigationDetails.setCreateTime(new Date());
                    advisoryNavigationDetails.setModifyTime(advisoryArticle.getModifyTime());
                    advisoryNavigationDetails.setNavigationId(Long.valueOf(str[i]));
                    advisoryNavigationDetailsMapper.insertSelective(advisoryNavigationDetails);
                }
            }
            return ApiResult.success();
        }
        return ApiResult.error();
    }

    @Override
    public MyPageInfo<AdvisoryArticleVo> ObjList(AdvisoryArticleListDto advisoryArticleListDto) {
        PageHelper.startPage(advisoryArticleListDto.getCurrentPage(), advisoryArticleListDto.getPageSize());
        if(advisoryArticleListDto.getCreateTime() != null && !"".equals(advisoryArticleListDto.getCreateTime())){
            advisoryArticleListDto.setCreateTime(advisoryArticleListDto.getCreateTime()+ " 00:00:00");
        }
        if(advisoryArticleListDto.getModifyTime() != null && !"".equals(advisoryArticleListDto.getModifyTime())){
            advisoryArticleListDto.setModifyTime(advisoryArticleListDto.getModifyTime()+ " 00:00:00");
        }
        List<AdvisoryArticleVo> advisoryBannerVoList = advisoryArticleMapper.objList(advisoryArticleListDto);
        MyPageInfo<AdvisoryArticleVo> pageInfo = new MyPageInfo<>(advisoryBannerVoList);
        return pageInfo;
    }

    @Override
    public ApiResult batchOperator(String id, String type) {
        AdvisoryNavigationDetails advisoryNavigationDetails = new AdvisoryNavigationDetails();
        Condition condition = new Condition(AdvisoryNavigationDetails.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("typeId",id);
        criteria.andEqualTo("type", "article");
        AdvisoryArticle advisoryArticle = new AdvisoryArticle();
        advisoryArticle.setId(Long.valueOf(id));
        advisoryArticle.setModifyTime(new Date());
        if ("delete".equals(type)) {
            if(advisoryArticleMapper.deleteByPrimaryKey(advisoryArticle) > 0 && advisoryNavigationDetailsMapper.deleteByCondition(condition) >0){
                return ApiResult.success();
            }
            return ApiResult.error();
        }
        if ("show".equals(type)) {
            advisoryArticle.setArticleDisplay(type);
        }
        if ("hidden".equals(type)) {
            advisoryArticle.setArticleDisplay(type);
        }
        if (advisoryArticleMapper.updateByPrimaryKeySelective(advisoryArticle) < 0) {
            return ApiResult.error();
        }
        return ApiResult.success();
    }

    @Override
    public AdvisoryArticleVo lookObj(long id) {
        Condition condition = new Condition(AdvisoryArticle.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", id);
        List<AdvisoryArticle> advisoryArticleList = advisoryArticleMapper.selectByCondition(condition);
        if (CollectionUtils.isEmpty(advisoryArticleList)) {
            return new AdvisoryArticleVo();
        }
        AdvisoryArticleVo advisoryArticleVo = BeanUtil.copyProperties(advisoryArticleList.get(0), AdvisoryArticleVo.class);
        return advisoryArticleVo;
    }

    @Override
    public int deleteRelationship(String typeId, String navigationId) {
        Condition condition = new Condition(AdvisoryArticle.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", typeId);
        List<AdvisoryArticle> advisoryArticles = advisoryArticleMapper.selectByCondition(condition);
        if(!CollectionUtils.isEmpty(advisoryArticles)){
            AdvisoryArticle advisoryArticle = advisoryArticles.get(0);
            if(advisoryArticle.getNavigationId().contains(navigationId)){
                String strArry[] = advisoryArticle.getNavigationId().split(",");
                if(!strArry[0].equals(navigationId)){
                    advisoryArticle.setNavigationId(advisoryArticle.getNavigationId().replace(","+navigationId,""));
                } else {
                    advisoryArticle.setNavigationId(advisoryArticle.getNavigationId().replace(navigationId+",",""));
                }
            }
            advisoryArticle.setModifyTime(new Date());
            if(advisoryArticleMapper.updateByPrimaryKeySelective(advisoryArticle) > 0){
                return 1;
            }
        }
        return 0;
    }


    private AdvisoryNavigationDetails operatorObj(AdvisoryArticle advisoryArticle){
        AdvisoryNavigationDetails advisoryNavigationDetails =new AdvisoryNavigationDetails();
        advisoryNavigationDetails.setFlag((byte) 1);
        advisoryNavigationDetails.setTypeId(advisoryArticle.getId());
        advisoryNavigationDetails.setTitle(advisoryArticle.getArticleTitle());
        advisoryNavigationDetails.setImage(advisoryArticle.getArticleImage());
        advisoryNavigationDetails.setUrl(advisoryArticle.getArticleUrl());
        advisoryNavigationDetails.setType("article");
        return advisoryNavigationDetails;
    }
}
