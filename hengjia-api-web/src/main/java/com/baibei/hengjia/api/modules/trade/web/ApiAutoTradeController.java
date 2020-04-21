package com.baibei.hengjia.api.modules.trade.web;

import com.baibei.hengjia.api.modules.trade.service.IAutoConfigService;
import com.baibei.hengjia.common.tool.api.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/9/23 11:22
 * @description: 自动购销
 */
@RestController
@RequestMapping("/api/autoTrade")
@Slf4j
public class ApiAutoTradeController {
    @Autowired
    private IAutoConfigService autoConfigService;

    /**
     * 定时任务，到达截止日期自动关闭预约购销
     *
     * @return
     */
    @GetMapping("/closingTime")
    public ApiResult closingTime() {
        log.info("关闭到达截止日期的预约购销定时任务正在执行...");
        autoConfigService.closingTime();
        return ApiResult.success();
    }

    /**
     * 定时任务，自动购销
     *
     * @return
     */
    @GetMapping("/trade")
    public ApiResult trade() {
        log.info("自动购销任务正在执行");
        long start = System.currentTimeMillis();
        ApiResult apiResult = autoConfigService.trade();
        log.info("自动购销任务执行完毕，耗时{}", (System.currentTimeMillis() - start));
        return apiResult;
    }
}