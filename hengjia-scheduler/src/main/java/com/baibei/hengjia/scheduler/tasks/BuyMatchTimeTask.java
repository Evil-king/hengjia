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
 * @date: 2019/8/17
 * @description: 买入配货定时器
 */
@Slf4j
@Component
@EnableScheduling
public class BuyMatchTimeTask {
    @Autowired
    private ApiFeign apiFeign;
    @Value("${execute.task.switch}")
    private String executeTaskSwitch;

    /**
     * 执行任务,每天15:30:00执行
     */
    @Scheduled(cron = "${buymatch.cron}")
    private void buymatchTask() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("buymatch");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("买入配货定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            log.info("buymatchTask start...");
            ApiResult apiResult = apiFeign.buyMatch();
            log.info("buymatchTask end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }

    /**
     * 系统自动提货执行任务,每天17:15:00执行
     */
  /*  @Scheduled(cron = "${sysDelivery.cron}")
    private void sysDelivery() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("sysDelivery");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("系统自动提货定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            log.info("sysDelivery start...");
            ApiResult apiResult = apiFeign.sysDelivery();
            log.info("sysDelivery end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }*/

    /**
     * 提货券,每天17:15:00执行
     */
  /*  @Scheduled(cron = "${deliveryTransfer.cron}")
    private void deliveryTransfer() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("deliveryTransfer");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("提货券定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            log.info("deliveryTransfer start...");
            ApiResult apiResult = apiFeign.deliveryTransfer();
            log.info("deliveryTransfer end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }*/

    /**
     * 提货券补扣,每天17:30:00执行
     */
    @Scheduled(cron = "${offsetDeliveryTicket.cron}")
    private void offsetDeliveryTicket() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("offsetDeliveryTicket");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("提货券补扣定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            log.info("offsetDeliveryTicket start...");
            ApiResult apiResult = apiFeign.offsetDeliveryTicket();
            log.info("offsetDeliveryTicket end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }

    /**
     * 将wait状态的配货失败单失效掉.每天早上开盘前执行
     */
    @Scheduled(cron = "${destoryFailLog.cron}")
    private void destoryFailLog() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("destoryFailLog");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("失效失败单定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            log.info("destoryFailLog start...");
            ApiResult apiResult = apiFeign.destoryFailLog();
            log.info("destoryFailLog end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }


    /**
     * 定时打开买入配货开关。每天15:15:00执行
     */
   /* @Scheduled(cron = "${open.buymatch.switch.cron}")
    private void controlSwitch() {
        JSONObject jsonObject = JSONObject.parseObject(executeTaskSwitch);
        String executeFlag = jsonObject.getString("buymatch");
        if (Constants.ScheduleSwitch.OFF.equals(executeFlag)) {
            log.info("买入配货，控制开关的定时任务已锁定，不执行相关逻辑");
        }
        if(Constants.ScheduleSwitch.ON.equals(executeFlag)){
            ControlSwitchDto controlSwitchDto = new ControlSwitchDto();
            controlSwitchDto.setSwtch(Constants.MatchSwitch.ON);
            controlSwitchDto.setSwitchType(Constants.SwitchType.BUYMATCH);
            log.info("buymatchTask start...");
            ApiResult apiResult = apiFeign.controlSwitch(controlSwitchDto);
            log.info("buymatchTask end,apiResult={}", JSON.toJSONString(apiResult));
        }
    }*/

    public static void main(String[] args) {
        String a = "{\"buymatch\":\"on\",\"deliverymatch\":\"off\"}";
        JSONObject jsonObject = JSONObject.parseObject(a);
        System.out.println(jsonObject.getString("buymatch"));
        System.out.println(jsonObject);
    }
}
