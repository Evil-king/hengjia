package com.baibei.hengjia.api.modules.advisory.dao;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryVideo;
import com.baibei.hengjia.common.core.mybatis.MyMapper;

public interface AdvisoryVideoMapper extends MyMapper<AdvisoryVideo> {

    AdvisoryVideoVo queryByParams(String id);

}