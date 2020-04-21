package com.baibei.hengjia.gateway;

import com.baibei.hengjia.gateway.filters.error.ErrorFilter;
import com.baibei.hengjia.gateway.filters.post.HttpLogFilter;
import com.baibei.hengjia.gateway.filters.post.ResponseWrapperFilter;
import com.baibei.hengjia.gateway.filters.pre.AuthenticationFilter;
import com.baibei.hengjia.gateway.filters.pre.CommonParamCheckFilter;
import com.baibei.hengjia.gateway.filters.pre.PostBodyWrapperFilter;
import com.baibei.hengjia.gateway.filters.pre.SignatureFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author: 会跳舞的机器人
 * @date: 2019/3/22 5:45 PM
 * @description: 网关启动主类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})// 由于引用了core包导致依赖了mybatis相关的jar,所以此处需要剔除
@EnableZuulProxy
@ComponentScan("com.baibei.hengjia")
@EnableFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        System.out.println("start execute GatewayApplication...\n");
        SpringApplication.run(GatewayApplication.class, args);
        System.out.println("end execute GatewayApplication...\n");
    }

    @Bean
    public CommonParamCheckFilter commonParamCheckFilter() {
        return new CommonParamCheckFilter();
    }

    @Bean
    public SignatureFilter signatureFilter() {
        return new SignatureFilter();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter();
    }

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

    @Bean
    public PostBodyWrapperFilter postBodyWrapperFilter() {
        return new PostBodyWrapperFilter();
    }

    @Bean
    public ResponseWrapperFilter responseWrapperFilter() {
        return new ResponseWrapperFilter();
    }

    @Bean
    public HttpLogFilter httpLogFilter() {
        return new HttpLogFilter();
    }
}
