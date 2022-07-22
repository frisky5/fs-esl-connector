package com.tsuki.fseslconnector.utilities;

import java.util.concurrent.atomic.AtomicBoolean;

public class FsEslStatusStore {
    private AtomicBoolean isFsEslSocketConnected = new AtomicBoolean(false);
    private AtomicBoolean isFsEslSocketAuthenticated = new AtomicBoolean(false);
    private String lastHeartbeatEpoch;
    private String freeswitchHostname;
    private String freeswitchIpAddress;

    public AtomicBoolean getIsFsEslSocketConnected() {
        return this.isFsEslSocketConnected;
    }

    public void setIsFsEslSocketConnected(Boolean isFsEslSocketConnected) {
        this.isFsEslSocketConnected.set(isFsEslSocketConnected);
    }

    public String getLastHeartbeatEpoch() {
        return this.lastHeartbeatEpoch;
    }

    public void setLastHeartbeatEpoch(String lastHeartBeat) {
        this.lastHeartbeatEpoch = lastHeartBeat;
    }

    public String getFreeswitchHostname() {
        return this.freeswitchHostname;
    }

    public void setFreeswitchHostname(String freeswitchHostname) {
        this.freeswitchHostname = freeswitchHostname;
    }

    public String getFreeswitchIpAddress() {
        return this.freeswitchIpAddress;
    }

    public void setFreeswitchIpAddress(String freeswitchIpAddress) {
        this.freeswitchIpAddress = freeswitchIpAddress;
    }

    public AtomicBoolean getIsFsEslSocketAuthenticated() {
        return this.isFsEslSocketAuthenticated;
    }

    public void setIsFsEslSocketAuthenticated(Boolean isFsEslSocketAuthenticated) {
        this.isFsEslSocketAuthenticated.set(isFsEslSocketAuthenticated);
    }

}
