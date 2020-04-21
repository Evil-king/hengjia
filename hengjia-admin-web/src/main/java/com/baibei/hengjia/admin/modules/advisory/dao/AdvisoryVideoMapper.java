package com.baibei.hengjia.admin.modules.advisory.dao;

import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryVideoListDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryVideo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

import java.util.List;

public interface AdvisoryVideoMapper extends MyMapper<AdvisoryVideo> {

    List<AdvisoryVideoVo> objList(AdvisoryVideoListDto advisoryVideoListDto);

    int updateByParams(AdvisoryVideo advisoryVideo);

}