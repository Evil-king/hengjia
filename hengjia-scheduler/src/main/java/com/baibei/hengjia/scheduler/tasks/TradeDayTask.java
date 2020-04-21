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
 * @date: 2019/6/14 10:04 AM
 * @description: 定时更新开休市状态
 */
@Slf4j
@Component
@EnableScheduling
public class TradeDayTask {

    @Autowired
    private ApiFeign apiFeign;

    /**
     * 执行任务
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void dealTask() {
        log.info("TradeDayTask start...");
        ApiResult apiResult = apiFeign.setTradeDay();
        log.info("TradeDayTask end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
