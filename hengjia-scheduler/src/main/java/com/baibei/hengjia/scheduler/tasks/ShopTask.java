package com.baibei.hengjia.scheduler.tasks;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.scheduler.feign.ApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/7/3 2:07 PM
 * @description: 扫描是否满足14天自动收货
 */
@Slf4j
@Component
@EnableScheduling
public class ShopTask {

    @Autowired
    private ApiFeign apiFeign;


    @Scheduled(cron = "0 0 0 * * ?")
    private void longDayConfirmSend() {
        log.info("longDayConfirmSend start...");
        ApiResult apiResult = apiFeign.longDayConfirmSend();
        log.info("longDayConfirmSend end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
