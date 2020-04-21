package com.baibei.hengjia.admin.modules.advisory.dao;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryArticleMapper extends MyMapper<AdvisoryArticle> {

    List<AdvisoryArticleVo> objList(AdvisoryArticleListDto advisoryArticleListDto);
}