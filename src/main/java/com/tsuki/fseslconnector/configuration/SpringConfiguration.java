package com.tsuki.fseslconnector.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tsuki.fseslconnector.FsEslClient;
import com.tsuki.fseslconnector.handlers.AuthenticationAndSubscribeHandler;
import com.tsuki.fseslconnector.handlers.EventsHandler;
import com.tsuki.fseslconnector.utilities.FsEslStatusStore;

@Configuration
public class SpringConfiguration {

    @Bean
    public FsEslStatusStore fsEslStatusStore() {
        return new FsEslStatusStore();
    }

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
    public EventsHandler eventsHandler(ThreadPoolTaskExecutor eventsProcessingPool, FsEslStatusStore fsEslStatusStore) {
        return new EventsHandler(eventsProcessingPool, fsEslStatusStore);
    }

    @Bean
    public AuthenticationAndSubscribeHandler authenticationHandler(EventsHandler eventsHandler,
            FsEslClientProperties fsEslClientProperties,
            FsEslStatusStore fsEslStatusStore) {
        return new AuthenticationAndSubscribeHandler(eventsHandler, fsEslClientProperties, fsEslStatusStore);
    }

    @Bean
    public FsEslClient fsEslClient(AuthenticationAndSubscribeHandler authenticationHandler,
            FsEslClientProperties fsEslClientProperties,
            FsEslStatusStore fsEslStatusStore) {
        return new FsEslClient(authenticationHandler, fsEslClientProperties, fsEslStatusStore);
    }
}
