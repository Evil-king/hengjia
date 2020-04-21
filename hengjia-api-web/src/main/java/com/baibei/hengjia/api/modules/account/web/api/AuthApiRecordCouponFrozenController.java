package com.baibei.hengjia.api.modules.account.web.api;

import com.baibei.hengjia.api.modules.account.bean.dto.RecordDto;
import com.baibei.hengjia.api.modules.account.bean.vo.RecordVo;
import com.baibei.hengjia.api.modules.account.service.IRecordCouponFrozenService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.page.MyPageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: hyc
 * @date: 2019/10/14 11:27
 * @description:
 */
@RestController
@RequestMapping("/auth/api/recordCouponFrozen")
public class AuthApiRecordCouponFrozenController {
    @Autowired
    private IRecordCouponFrozenService recordCouponFrozenService;

    @PostMapping("/getAll")
    @ResponseBody
    public ApiResult<MyPageInfo<RecordVo>> getAll(@RequestBody @Validated RecordDto recordCouponDto){
        return ApiResult.success(recordCouponFrozenService.getAll(recordCouponDto));
    }
}
