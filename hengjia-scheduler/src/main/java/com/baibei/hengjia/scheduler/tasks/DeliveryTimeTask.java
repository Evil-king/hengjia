package com.baibei.hengjia.scheduler.tasks;

import com.baibei.hengjia.scheduler.feign.ApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 收货定时任务
 */
@Slf4j
@Component
@EnableScheduling
public class DeliveryTimeTask {

    @Autowired
    private ApiFeign apiFeign;

    @Scheduled(cron = "${delivery.receipt}")
    private void autoReceipt(){
        log.info("scheduling autoReceipt start run ..................");
        apiFeign.autoReceipt();
        log.info("scheduling autoReceipt end ........................");
    }
}
