package com.baibei.hengjia.scheduler.tasks;

import com.baibei.hengjia.common.tool.api.ApiResult;
import com.baibei.hengjia.common.tool.api.ResultEnum;
import com.baibei.hengjia.scheduler.feign.ApiFeign;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@EnableScheduling
public class SigningInBackTask {

    @Autowired
    private ApiFeign apiFeign;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 关闭自动签到
     */
    @Value("${closeAutoSign}")
    private Boolean closeAutoSign;

    @Scheduled(cron = "${cash.signIn.cron}")
    private void signInTask() {
        if (!closeAutoSign) {
            ApiResult apiResult = apiFeign.signIn();
            if (ResultEnum.SUCCESS.getCode() != apiResult.getCode()) {
                logger.info("签到失败:错误Code为{},错误描述为{}", apiResult.getCode(), apiResult.getMsg());
            }
        }
    }

    /* @Scheduled(cron = "${cash.signBack.cron}")*/
    private void signBack() {
        ApiResult apiResult = apiFeign.signBack();
        if (ResultEnum.SUCCESS.getCode() != apiResult.getCode()) {
            logger.info("签到失败:错误Code为{},错误描述为{}", apiResult.getCode(), apiResult.getMsg());
        }
    }
}
