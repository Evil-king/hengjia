package com.baibei.hengjia.admin.modules.settlement.web;

import com.baibei.hengjia.common.tool.api.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @GetMapping("/demo")
    public ApiResult demo() {
        System.out.println("输出一句话");
        return ApiResult.success();
    }
}
