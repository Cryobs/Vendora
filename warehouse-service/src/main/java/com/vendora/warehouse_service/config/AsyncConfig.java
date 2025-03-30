package com.vendora.warehouse_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "updateStockImportExecutor")
    public Executor updateStockImportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Number of persistent threads
        executor.setMaxPoolSize(50);  // Max number of threads
        executor.setQueueCapacity(100); // Task queue (if no free)
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "processBatchStockImportExecutor")
    public Executor processBatchStockImportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Number of persistent threads
        executor.setMaxPoolSize(50);  // Max number of threads
        executor.setQueueCapacity(100); // Task queue (if no free)
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }
}
