package com.baibei.hengjia.api.modules.advisory.service.impl;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.api.modules.advisory.dao.AdvisoryArticleMapper;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.api.modules.advisory.service.IAdvisoryArticleService;
import com.baibei.hengjia.common.core.mybatis.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

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


    @Override
    public AdvisoryArticleVo details(String id) {
        AdvisoryArticleVo advisoryArticleVos = advisoryArticleMapper.objIndex(id);
        return advisoryArticleVos;
    }

    @Override
    public AdvisoryArticle selectByParams(String id) {
        Condition condition = new Condition(AdvisoryArticle.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("id", id);
        criteria.andEqualTo("articleType", "url");
        criteria.andEqualTo("articleDisplay", "show");
        List<AdvisoryArticle> advisoryArticles = advisoryArticleMapper.selectByCondition(condition);
        if(!CollectionUtils.isEmpty(advisoryArticles)){
            return advisoryArticles.get(0);
        }
        return null;
    }
}
