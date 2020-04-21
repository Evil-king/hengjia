package com.baibei.hengjia.api.modules.advisory.dao;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryBanner;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryBannerMapper extends MyMapper<AdvisoryBanner> {

    List<AdvisoryBannerVo> objIndex();
}