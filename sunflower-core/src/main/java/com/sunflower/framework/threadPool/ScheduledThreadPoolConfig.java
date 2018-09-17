package com.sunflower.framework.threadPool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

@Configuration
public class ScheduledThreadPoolConfig {
    public ScheduledThreadPoolConfig() {
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledExecutorService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        return executor;
    }
}