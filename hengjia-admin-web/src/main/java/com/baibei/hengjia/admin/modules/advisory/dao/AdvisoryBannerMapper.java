package com.baibei.hengjia.admin.modules.advisory.dao;


import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryBannerListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryBanner;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryBannerMapper extends MyMapper<AdvisoryBanner> {

    List<AdvisoryBannerVo> objList(AdvisoryBannerListDto advisoryBannerListDto);

    List<AdvisoryBannerVo> objIndex();

    int isDisplayFive();

    int updateByParams(AdvisoryBanner advisoryBanner);

}