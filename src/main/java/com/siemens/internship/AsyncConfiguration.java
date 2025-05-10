package com.siemens.internship;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * custom bean definition for a ThreadPoolTaskExecutor class instance as it is managed by spring
 * --he is better than myself at managing staff-- and we had a thread pool constraint of 10 running
 * threads running at the same time
 */
@Configuration
public class AsyncConfiguration {

    @Bean("itemExecutor")
    public ThreadPoolTaskExecutor itemExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ItemExecutor-");
        executor.initialize();
        return executor;
    }
}
