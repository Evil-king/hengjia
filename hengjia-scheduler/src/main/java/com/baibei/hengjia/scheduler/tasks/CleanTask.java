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
 * @date: 2019/7/3 3:40 PM
 * @description: 清算定时任务
 */
@Slf4j
@Component
@EnableScheduling
public class CleanTask {
    /*@Autowired
    private ApiFeign apiFeign;


    @Scheduled(cron = "${clean.cron}")
    private void dealTask() {
        log.info("CleanTask start...");
        ApiResult apiResult = apiFeign.clean();
        log.info("CleanTask end,apiResult={}", JSON.toJSONString(apiResult));
    }*/
}
