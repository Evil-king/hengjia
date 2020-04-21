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
 * @description: 预约购销截止日期后关闭
 */
@Slf4j
@Component
@EnableScheduling
public class AutoTradeClosingTimeTask {

    @Autowired
    private ApiFeign apiFeign;


    @Scheduled(cron = "${auto.trade.closing.cron}")
    private void dealTask() {
        log.info("AutoTradeClosingTimeTask start...");
        ApiResult apiResult = apiFeign.closingTime();
        log.info("AutoTradeClosingTimeTask end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
