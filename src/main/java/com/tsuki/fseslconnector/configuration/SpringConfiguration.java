package com.tsuki.fseslconnector.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tsuki.fseslconnector.FsEslClient;
import com.tsuki.fseslconnector.handlers.AuthenticationAndSubscribeHandler;
import com.tsuki.fseslconnector.handlers.EventsHandler;

@Configuration
public class SpringConfiguration {

    @Bean
    public ThreadPoolTaskExecutor eventsProcessingPool() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean
    public EventsHandler eventsHandler(ThreadPoolTaskExecutor eventsProcessingPool) {
        return new EventsHandler(eventsProcessingPool);
    }

    @Bean
    public AuthenticationAndSubscribeHandler authenticationHandler(EventsHandler eventsHandler,
            FsEslClientProperties fsEslClientProperties) {
        return new AuthenticationAndSubscribeHandler(eventsHandler, fsEslClientProperties);
    }

    @Bean
    public FsEslClient fsEslClient(AuthenticationAndSubscribeHandler authenticationHandler,
            FsEslClientProperties fsEslClientProperties) {
        return new FsEslClient(authenticationHandler, fsEslClientProperties);
    }
}
