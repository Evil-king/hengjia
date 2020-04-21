package com.baibei.hengjia.admin.modules.advisory.service;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryArticleListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryArticleVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryArticle;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryArticle服务接口
*/
public interface IAdvisoryArticleService extends Service<AdvisoryArticle> {

    ApiResult addAndEdit(AdvisoryArticleDto advisoryArticleDto);

    MyPageInfo<AdvisoryArticleVo> ObjList(AdvisoryArticleListDto advisoryArticleListDto);

    ApiResult batchOperator(String id, String type);

    AdvisoryArticleVo lookObj(long id);

    int deleteRelationship(String typeId, String navigationId);
}
