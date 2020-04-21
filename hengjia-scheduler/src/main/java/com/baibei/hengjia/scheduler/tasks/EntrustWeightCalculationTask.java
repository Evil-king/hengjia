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
 * @date: 2019/9/7 4:54 PM
 * @description: 每日休市后计算客户挂单的权重定时任务
 */
@Slf4j
@Component
@EnableScheduling
public class EntrustWeightCalculationTask {
    @Autowired
    private ApiFeign apiFeign;


    @Scheduled(cron = "0 0 17 * * ?")
    private void dealTask() {
        log.info("EntrustWeightCalculationTask start...");
        ApiResult apiResult = apiFeign.calculation();
        log.info("EntrustWeightCalculationTask end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
