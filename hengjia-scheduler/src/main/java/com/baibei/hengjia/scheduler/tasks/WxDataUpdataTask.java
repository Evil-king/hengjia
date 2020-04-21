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
 * @date: 2019/6/17 6:12 PM
 * @description: 定时更新微信的access_token, jsapi_ticket
 */
@Slf4j
@Component
@EnableScheduling
public class WxDataUpdataTask {
    @Autowired
    private ApiFeign apiFeign;


    /**
     * 处理逻辑
     */
    @Scheduled(fixedRate = 1000 * 60 * 90, initialDelay = 2000)//项目启动2秒中之后执行一次，然后每90min执行一次
    private void dealTask() {
        log.info("WxDataUpdataTask start...");
        ApiResult apiResult = apiFeign.updateWxInfo();
        log.info("WxDataUpdataTask end,apiResult={}", JSON.toJSONString(apiResult));
    }

}
