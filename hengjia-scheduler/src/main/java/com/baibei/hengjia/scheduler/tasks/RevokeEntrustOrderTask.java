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
 * @date: 2019/10/10 18:58
 * @description:
 */
//@Slf4j
//@Component
//@EnableScheduling
public class RevokeEntrustOrderTask {

    /*@Autowired
    private ApiFeign apiFeign;


    @Scheduled(cron = "${revoke.cron}")
    private void dealTask() {
        log.info("RevokeEntrustOrderTask start...");
        ApiResult apiResult = apiFeign.revokeAll();
        log.info("RevokeEntrustOrderTask end,apiResult={}", JSON.toJSONString(apiResult));
    }*/
}