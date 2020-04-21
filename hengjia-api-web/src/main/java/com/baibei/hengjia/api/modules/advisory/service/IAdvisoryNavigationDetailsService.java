package com.baibei.hengjia.api.modules.advisory.service;

import com.baibei.hengjia.api.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationDetailsVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;


/**
* @author: wenq
* @date: 2019/09/14 09:47:23
* @description: AdvisoryNavigationDetails服务接口
*/
public interface IAdvisoryNavigationDetailsService extends Service<AdvisoryNavigationDetails> {

    MyPageInfo<AdvisoryNavigationDetailsVo> navigationDetails(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto);

}
