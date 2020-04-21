package com.baibei.hengjia.scheduler.tasks;

import com.alibaba.fastjson.JSON;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.scheduler.feign.ApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class ChangeCouponAndIntegralAmountTask {
    @Autowired
    private ApiFeign apiFeign;

    @Scheduled(cron = "${change.coupon.integral.cron}")
    private void changeCouponAndIntegralAmountTask() {
        log.info("changeCouponAndIntegralAmountTask start...");
        ApiResult apiResult = apiFeign.changeCouponAndIntegralAmount();
        log.info("changeCouponAndIntegralAmountTask end,apiResult={}", JSON.toJSONString(apiResult));
    }
}
