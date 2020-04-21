package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.bean.dto.MatchPlanDetailDto;
import com.baibei.hengjia.api.modules.match.model.MatchPlanDetail;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
* @author: Longer
* @date: 2019/08/07 17:57:17
* @description: MatchPlanDetail服务接口
*/
public interface IMatchPlanDetailService extends Service<MatchPlanDetail> {
    /**
     * 插入一条数据
     * @param matchPlanDetailDto
     * @param id 计划ID
     * @return
     */
    ApiResult insertList(MatchPlanDetailDto matchPlanDetailDto, Long id);

    /**
     * 通过计划id以及状态找到所有明细
     * @param id
     * @param status
     * @return
     */
    List<MatchPlanDetail> findbyPlanId(Long id, String status);

    /**
     * 根据id以及用户编号获取明细
     * @param customerNo
     * @param id
     * @return
     */
    List<MatchPlanDetail> findByCustomerNoAndPlanId(String customerNo, Long id);
}
