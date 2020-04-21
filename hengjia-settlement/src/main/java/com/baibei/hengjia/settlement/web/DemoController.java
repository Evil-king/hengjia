package com.baibei.hengjia.settlement.web;

import com.baibei.hengjia.common.core.redis.RedisUtil;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.settlement.service.IBankOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/21 5:21 PM
 * @description:
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IBankOrderService bankOrderService;

    @GetMapping("/pub")
    public ApiResult pub() {
        redisUtil.pub("clean", "it is time to clean");

        return ApiResult.success();
    }

    @PostMapping("/bankOrder")
    public ApiResult bankOrder(){
        bankOrderService.bankOrder(null);
        return ApiResult.success();
    }
}
