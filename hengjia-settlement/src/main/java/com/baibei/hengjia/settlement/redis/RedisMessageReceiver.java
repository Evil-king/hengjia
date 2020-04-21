package com.baibei.hengjia.settlement.redis;

import com.baibei.hengjia.settlement.biz.CleanBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/6/21 6:19 PM
 * @description: Redis订阅消息接收器
 */
@Component
@Slf4j
public class RedisMessageReceiver {
    @Autowired
    private CleanBiz cleanBiz;

    /**
     * 出入金对账
     *
     * @param msg
     */
    public void withdrawDeposit(String msg) {
        log.info("正在执行出入金对账 msg={}", msg);

    }

    /**
     * 清算
     *
     * @param msg
     */
    public void clean(String msg) {
        log.info("正在执行清算,msg={}", msg);
        cleanBiz.clean();
    }
}
