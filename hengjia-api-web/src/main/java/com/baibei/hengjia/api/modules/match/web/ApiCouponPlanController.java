package com.baibei.hengjia.api.modules.match.web;

import com.baibei.hengjia.api.modules.match.bean.dto.CouponPlanDto;
import com.baibei.hengjia.api.modules.match.bean.dto.CouponPlanExecuteDto;
import com.baibei.hengjia.api.modules.match.bean.vo.CouponPlanVo;
import com.baibei.hengjia.api.modules.match.dao.CouponPlanMapper;
import com.baibei.hengjia.api.modules.match.model.CouponPlan;
import com.baibei.hengjia.api.modules.match.service.ICouponPlanService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: hyc
 * @date: 2019/8/8 9:45
 * @description:
 */
@RestController
@RequestMapping("/api/couponPlan")
@Slf4j
public class ApiCouponPlanController {
    @Autowired
    private ICouponPlanService couponPlanService;

    @PostMapping("/import")
    public ApiResult<CouponPlanVo> importCouponPlan(@RequestBody @Validated CouponPlanDto couponPlanDto){
        return couponPlanService.importCouponPlan(couponPlanDto);
    }

    @PostMapping("/execute")
    public ApiResult execute(@RequestBody @Validated CouponPlanExecuteDto couponPlanExecuteDto){
        return couponPlanService.execute(couponPlanExecuteDto);
    }
}
