package com.baibei.hengjia.api.modules.account.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.service.ICouponAccountService;
import com.baibei.hengjia.api.modules.account.service.IRecordCouponService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyc
 * @date: 2019/8/5 10:09
 * @description:
 */
@RestController
@RequestMapping("/auth/api/recordCoupon")
public class AuthApiRecordCouponController {

    @Autowired
    private IRecordCouponService recordCouponService;

    @PostMapping("/getAll")
    @ResponseBody
    public ApiResult<MyPageInfo<RecordVo>> getAll(@RequestBody @Validated RecordDto recordCouponDto){
        return ApiResult.success(recordCouponService.getAll(recordCouponDto));
    }
}
