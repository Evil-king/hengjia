package com.baibei.hengjia.settlement;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @author: 会跳舞的机器人
 * @date: 2019/5/23 5:36 PM
 * @description:
 */
@SpringCloudApplication
@EnableTransactionManagement
@MapperScan(basePackages = {"com.baibei.hengjia.settlement.dao"})
@ComponentScan("com.baibei.hengjia")
@EnableHystrix
public class SettlementApplication {
    public static void main(String[] args) {
        System.out.println("start execute SettlementApplication...\n");
        SpringApplication.run(SettlementApplication.class, args);
        System.out.println("end execute SettlementApplication...\n");
    }

}
