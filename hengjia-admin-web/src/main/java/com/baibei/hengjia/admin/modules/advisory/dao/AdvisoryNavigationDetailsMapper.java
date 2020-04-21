package com.baibei.hengjia.admin.modules.advisory.dao;


import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationDetailVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryNavigationDetailsMapper extends MyMapper<AdvisoryNavigationDetails> {

    List<AdvisoryNavigationDetailVo> objList(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto);

    List<AdvisoryNavigationDetailVo> objListByNavigationId(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto);

    int updateByParams(AdvisoryNavigationDetails advisoryNavigationDetails);

}