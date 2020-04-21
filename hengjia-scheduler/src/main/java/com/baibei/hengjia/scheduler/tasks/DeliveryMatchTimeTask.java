package com.baibei.hengjia.scheduler.tasks;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.constants.Constants;
import com.baibei.hengjia.scheduler.feign.ApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: Longer
 * @date: 2019/6/13
 * @description: 提货配票定时器
 */
@Slf4j
@Component
@EnableScheduling
public class DeliveryMatchTimeTask {
    @Autowired
    private ApiFeign apiFeign;

    @Value("${execute.task.switch}")
    private String executeTaskSwitch;

    /**
     * 执行任务,每分钟跑一次执行
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void dealTask() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("deliverymatch");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("提货配票定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            log.info("DeliveryMatchTimeTask start...");
            ApiResult apiResult = apiFeign.deliveryMatch();
            log.info("DeliveryMatchTimeTask end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }
}
