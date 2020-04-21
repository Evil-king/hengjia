package com.baibei.hengjia.api.modules.match.web;

import com.baibei.hengjia.api.modules.match.bean.dto.OffsetCouponDto;
import com.baibei.hengjia.api.modules.match.service.ICouponPlanService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Longer
 * @date: 2019/8/21 17:13
 * @description:抵扣消费
 */
@RestController
@RequestMapping("/api/couponOffset")
@Slf4j
public class ApiCouponOffsetController {
    @Autowired
    private ICouponPlanService couponPlanService;

    @PostMapping("/offset")
    public ApiResult importCouponPlan(@RequestBody @Validated List<OffsetCouponDto> offsetCouponDtoList){
        couponPlanService.offsetCoupon(offsetCouponDtoList);
        return ApiResult.success();
    }


}
