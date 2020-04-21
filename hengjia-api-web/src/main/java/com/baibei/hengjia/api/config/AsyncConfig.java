package com.baibei.hengjia.api.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Classname AsyncConfig
 * @Description 异步调用，自定义线程池
 * @Date 2019/10/16 15:36
 * @Created by Longer
 */
@Configuration
public class AsyncConfig {
    @Value("${threadPoolTaskExecutor.corePoolSize}")
    private int corePoolSize;//核心线程数
    @Value("${threadPoolTaskExecutor.maxPoolSiz}")
    private int maxPoolSiz;//最大线程数
    @Value("${threadPoolTaskExecutor.queueCapacity}")
    private int queueCapacity;//缓存队列容量
    @Value("${threadPoolTaskExecutor.keepAliveSeconds}")
    private int keepAliveSeconds;//线程活跃时间
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);        // 设置核心线程数
        executor.setMaxPoolSize(maxPoolSiz);        // 设置最大线程数
        executor.setQueueCapacity(queueCapacity);      // 设置队列容量
        executor.setKeepAliveSeconds(keepAliveSeconds);   // 设置线程活跃时间（秒）
        executor.setThreadNamePrefix("async-rpt-");  // 设置默认线程名称
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());// 设置拒绝策略
        executor.setWaitForTasksToCompleteOnShutdown(true); // 等待所有任务结束后再关闭线程池
        return executor;
    }
}
