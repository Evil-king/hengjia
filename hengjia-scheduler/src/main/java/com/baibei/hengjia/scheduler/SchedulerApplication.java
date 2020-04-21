package com.baibei.hengjia.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/22 5:45 PM
 * @description: 定时任务
 */
@SpringCloudApplication
@ComponentScan("com.baibei.hengjia")
@EnableFeignClients
public class SchedulerApplication {
    public static void main(String[] args) {
        System.out.println("start execute SchedulerApplication...\n");
        SpringApplication.run(SchedulerApplication.class, args);
        System.out.println("end execute SchedulerApplication...\n");
    }


}
