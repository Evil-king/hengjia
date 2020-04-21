package com.baibei.hengjia.api.modules.match.service;
import com.baibei.hengjia.api.modules.match.bean.dto.CouponPlanDto;
import com.baibei.hengjia.api.modules.match.bean.dto.CouponPlanExecuteDto;
import com.baibei.hengjia.api.modules.match.bean.dto.OffsetCouponDto;
import com.baibei.hengjia.api.modules.match.bean.vo.CouponPlanVo;
import com.baibei.hengjia.api.modules.match.model.CouponPlan;
import com.baibei.hengjia.common.core.mybatis.Service;
import com.baibei.hengjia.common.tool.api.ApiResult;

import java.util.List;


/**
* @author: Longer
* @date: 2019/08/07 17:57:17
* @description: CouponPlan服务接口
*/
public interface ICouponPlanService extends Service<CouponPlan> {

    ApiResult<CouponPlanVo> importCouponPlan(CouponPlanDto couponPlanDto);

    ApiResult execute(CouponPlanExecuteDto couponPlanExecuteDto);

    /**
     *  零售商品兑换券余额抵扣折扣商品
     * @param offsetCouponDtoList
     */
    void offsetCoupon(List<OffsetCouponDto> offsetCouponDtoList);
}
