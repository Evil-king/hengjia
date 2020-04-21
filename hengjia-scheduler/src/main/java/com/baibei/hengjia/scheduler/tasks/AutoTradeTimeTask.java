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
 * @description: 自动预约购销
 */
@Slf4j
@Component
@EnableScheduling
public class AutoTradeTimeTask {

    @Autowired
    private ApiFeign apiFeign;


    @Scheduled(cron = "${auto.trade.cron}")
    private void dealTask() {
        log.info("自动预约购销定时任务正在执行...");
        ApiResult apiResult = apiFeign.trade();
        log.info("自动预约购销定时任务执行结束，apiResult={}", JSON.toJSONString(apiResult));
    }
}
