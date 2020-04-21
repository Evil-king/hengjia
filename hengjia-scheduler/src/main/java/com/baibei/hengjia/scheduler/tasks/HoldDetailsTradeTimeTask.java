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
 * @date: 2019/6/10 7:43 PM
 * @description: 客户商品持仓明细达到交易日期之后需要增加商品的可卖数量
 */
@Slf4j
@Component
@EnableScheduling
public class HoldDetailsTradeTimeTask {
    @Autowired
    private ApiFeign apiFeign;

    /**
     * 执行任务,每天凌晨一点执行
     */
    //@Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0 */1 * * * ?")
    private void dealTask() {
        log.info("HoldDetailsTradeTimeTask start...");
        ApiResult apiResult = apiFeign.tradeTimeDeal();
        log.info("HoldDetailsTradeTimeTask end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
