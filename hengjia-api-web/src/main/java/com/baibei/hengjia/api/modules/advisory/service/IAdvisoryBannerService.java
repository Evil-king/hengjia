package com.baibei.hengjia.api.modules.advisory.service;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryBannerVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryBanner;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: wenq
* @date: 2019/09/11 10:09:00
* @description: AdvisoryBanner服务接口
*/
public interface IAdvisoryBannerService extends Service<AdvisoryBanner> {

    /**
     * 首页显示
     * @return
     */
    List<AdvisoryBannerVo> index();


}
