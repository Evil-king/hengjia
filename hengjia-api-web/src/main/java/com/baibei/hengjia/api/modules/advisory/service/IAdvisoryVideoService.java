package com.baibei.hengjia.api.modules.advisory.service;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryVideoVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryVideo;
import com.baibei.hengjia.common.core.mybatis.Service;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryVedio服务接口
*/
public interface IAdvisoryVideoService extends Service<AdvisoryVideo> {

    AdvisoryVideoVo details(String id);

}
