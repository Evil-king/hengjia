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
 * @author: hyc
 * @date: 2019/6/15 16:19
 * @description:
 */
@Slf4j
@Component
@EnableScheduling
public class WithdrawCashTimeTask {
    @Autowired
    private ApiFeign apiFeign;

    /**
     * 执行任务：可出金额等于可用资金（后续需要加入清算状态判断），晚上11点执行
     */
    @Scheduled(cron = "0 0 23 * * ?")
    private void dealTask() {
        log.info("WithdrawCashTimeTask start...");
        ApiResult apiResult = apiFeign.updateWithdrawTiming();
        log.info("WithdrawCashTimeTask end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
