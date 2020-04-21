package com.baibei.hengjia.admin.modules.advisory.service;


import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryVideo;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryVedio服务接口
*/
public interface IAdvisoryVideoService extends Service<AdvisoryVideo> {

    MyPageInfo<AdvisoryVideoVo> objList(AdvisoryVideoListDto advisoryVideoListDto);

    ApiResult addAndEdit(AdvisoryVideoDto advisoryVideoDto);

    AdvisoryVideoVo lookObj(long id);

    ApiResult batchOperator(String id, String type);

    int deleteRelationship(String typeId,String navigationId,String type);
}
