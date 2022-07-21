package com.tsuki.fseslconnector.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fsesl")
public class FsEslClientProperties {

    private String ip;
    private int port;
    private String password;
    private String events;

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEvents() {
        return this.events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

}