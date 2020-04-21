package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.bean.dto.TradeListDto;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.bean.CustomerBaseDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/10 9:43 AM
 * @description:
 */
@RestController
@RequestMapping("/auth/api")
public class AuthDemoController {


    @PostMapping("/demo")
    public ApiResult demo(@RequestBody @Validated TradeListDto customerBaseDto) {
        return ApiResult.success(customerBaseDto);
    }
}
