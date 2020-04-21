package com.baibei.hengjia.admin.modules.advisory.dao;


import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigation;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryNavigationMapper extends MyMapper<AdvisoryNavigation> {

    List<AdvisoryNavigationVo> objList(AdvisoryNavigationListDto advisoryNavigationListDto);

    int queryByNavigationId(long navigationId);

    int compareNavigationName(String navigationName);

    int isDisplayTen();
}