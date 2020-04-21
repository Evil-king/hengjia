package com.baibei.hengjia.admin.modules.advisory.service;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationListVo;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigation;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

import java.util.List;


/**
* @author: wenq
* @date: 2019/09/12 14:45:44
* @description: AdvisoryNavigation服务接口
*/
public interface IAdvisoryNavigationService extends Service<AdvisoryNavigation> {

    ApiResult addObj(AdvisoryNavigationDto advisoryNavigationDto);

    MyPageInfo<AdvisoryNavigationVo> objList(AdvisoryNavigationListDto advisoryNavigationListDto);

    ApiResult batchDelete(String id);

    int queryByNavigationId(String navigationId);

    List<AdvisoryNavigationListVo> navigationList();

    boolean compareNavigationName(String navigationName);
}
