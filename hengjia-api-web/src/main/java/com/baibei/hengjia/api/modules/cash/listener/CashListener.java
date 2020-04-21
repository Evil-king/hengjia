package com.baibei.hengjia.api.modules.cash.listener;

import com.baibei.hengjia.api.modules.cash.component.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class CashListener implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(CashListener.class);

    @Autowired
    private Server server;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("----------------启动监管系统服务监听-------------------");
        server.start();
    }
}
