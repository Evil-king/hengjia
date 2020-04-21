package com.baibei.hengjia.api.modules.advisory.service;

import com.baibei.hengjia.api.modules.advisory.bean.vo.AdvisoryNavigationListVo;
import com.baibei.hengjia.api.modules.advisory.model.AdvisoryNavigation;
import com.baibei.hengjia.common.core.mybatis.Service;

import java.util.List;


/**
* @author: wenq
* @date: 2019/09/12 14:45:44
* @description: AdvisoryNavigation服务接口
*/
public interface IAdvisoryNavigationService extends Service<AdvisoryNavigation> {

    List<AdvisoryNavigationListVo> navigationList();

}
