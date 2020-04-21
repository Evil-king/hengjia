package com.baibei.hengjia.api.modules.advisory.service;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryArticle服务接口
*/
public interface IAdvisoryArticleService extends Service<AdvisoryArticle> {

    AdvisoryArticleVo details(String id);

    AdvisoryArticle selectByParams(String id);
}
