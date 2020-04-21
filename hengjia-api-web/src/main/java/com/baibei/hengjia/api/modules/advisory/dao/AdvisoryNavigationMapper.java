package com.baibei.hengjia.api.modules.advisory.dao;

import com.baibei.hengjia.api.modules.advisory.model.AdvisoryNavigation;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryNavigationMapper extends MyMapper<AdvisoryNavigation> {

    List<AdvisoryNavigation> selectByParams();

}