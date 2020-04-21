package com.baibei.hengjia.admin.modules.advisory.service;


import com.baibei.hengjia.admin.modules.advisory.bean.dto.AdvisoryNavigationDetailsDto;
import com.baibei.hengjia.admin.modules.advisory.bean.dto.EditSortDto;
import com.baibei.hengjia.admin.modules.advisory.bean.vo.AdvisoryNavigationDetailVo;
import com.baibei.hengjia.admin.modules.advisory.model.AdvisoryNavigationDetails;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.page.MyPageInfo;

/**
* @author: wenq
* @date: 2019/09/14 09:47:23
* @description: AdvisoryNavigationDetails服务接口
*/
public interface IAdvisoryNavigationDetailsService extends Service<AdvisoryNavigationDetails> {

    MyPageInfo<AdvisoryNavigationDetailVo> objList(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto);

    int editSort(EditSortDto editSortDto);

    int deleteDetails(String id);

    MyPageInfo<AdvisoryNavigationDetailVo> objListByNavigationId(AdvisoryNavigationDetailsDto advisoryNavigationDetailsDto);

}
