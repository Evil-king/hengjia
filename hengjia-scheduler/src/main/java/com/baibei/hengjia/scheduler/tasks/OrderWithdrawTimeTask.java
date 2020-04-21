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
 * @author hwq
 * @date 2019/06/11
 * <p>
 *     平安出金定时任务
 * </p>
 */
@Slf4j
@Component
@EnableScheduling
public class OrderWithdrawTimeTask {

    @Autowired
    private ApiFeign apiFeign;

    /**
     * 执行任务
     * 5分钟一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    private void dealTask() {
        log.info("OrderWithdrawTimeTask 1325接口 start...");
        ApiResult apiResult = apiFeign.queryData();
        log.info("OrderWithdrawTimeTask end,apiResult={}", JSON.toJSONString(apiResult));
    }

    /**
     * 执行任务
     * 10分钟一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void findStatusTask() {
        log.info("findStatusTask start...");
        ApiResult apiResult = apiFeign.findStatusTask();
        log.info("findStatusTask end,apiResult={}", JSON.toJSONString(apiResult));
    }

    /**
     * 执行任务
     * 每天晚上6点执行一次
     */
    @Scheduled(cron = "0 0 18 * * ?")
    private void queryMemberBlance() {
        log.info("queryMemberBlance start...");
        ApiResult apiResult = apiFeign.queryMemberBlance();
        log.info("queryMemberBlance end,apiResult={}", JSON.toJSONString(apiResult));
    }

}
