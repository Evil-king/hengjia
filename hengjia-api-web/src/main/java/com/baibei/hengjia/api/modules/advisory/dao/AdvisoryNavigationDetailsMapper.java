package com.baibei.hengjia.api.modules.advisory.dao;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationDetailsVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryNavigationDetailsMapper extends MyMapper<AdvisoryNavigationDetails> {

    List<AdvisoryNavigationDetailsVo> navigationDetails(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto);

}