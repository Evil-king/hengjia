package com.baibei.hengjia.api.modules.account.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.CouponAccountDetailDto;
import com.baibei.hengjia.api.modules.account.bean.dto.ThawCouponAccountDto;
import com.baibei.hengjia.api.modules.account.bean.vo.CouponAccountDetailVo;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: hyc
 * @date: 2019/10/10 15:56
 * @description:
 */
@RestController
@RequestMapping("/auth/api/account/couponAccount")
@Slf4j
public class AuthApiCouponAccountController {
    @Autowired
    private ICouponAccountService couponAccountService;

    @RequestMapping("/thawCouponAccount")
    @ResponseBody
    public ApiResult thawCouponAccount(@RequestBody @Validated ThawCouponAccountDto thawCouponAccountDto){
        return couponAccountService.thawCouponAccount(thawCouponAccountDto);
    }

    @RequestMapping("/getCouponAccountDetail")
    @ResponseBody
    public ApiResult<CouponAccountDetailVo> getCouponAccountDetail(@RequestBody @Validated CouponAccountDetailDto couponAccountDetailDto){
        return ApiResult.success(couponAccountService.getCouponAccountDetail(couponAccountDetailDto));
    }
}
