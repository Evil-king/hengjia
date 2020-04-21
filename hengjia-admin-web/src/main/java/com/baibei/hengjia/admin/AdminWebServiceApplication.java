package com.baibei.hengjia.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/4/14 3:41 PM
 * @description:
 */
@SpringCloudApplication
@EnableTransactionManagement
@ComponentScan("com.baibei.hengjia")
@MapperScan(basePackages = {"com.baibei.hengjia.admin.modules.*.dao"})
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients
public class AdminWebServiceApplication {
    public static void main(String[] args) {
        System.out.println("start execute AdminWebServiceApplication...\n");
        SpringApplication.run(AdminWebServiceApplication.class, args);
        System.out.println("end execute AdminWebServiceApplication...\n");
    }

}
