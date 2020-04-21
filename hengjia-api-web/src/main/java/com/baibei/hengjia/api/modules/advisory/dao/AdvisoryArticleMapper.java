package com.baibei.hengjia.api.modules.advisory.dao;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryArticleMapper extends MyMapper<AdvisoryArticle> {

    AdvisoryArticleVo objIndex(String id);
}