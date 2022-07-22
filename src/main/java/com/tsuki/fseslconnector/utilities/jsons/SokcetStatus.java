package com.tsuki.fseslconnector.utilities.jsons;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "freeswitch-hostname",
        "freeswitch-ip",
        "last-heartbeat",
        "socket-status"
})
public class SokcetStatus {
    @JsonProperty("freeswitch-hostname")
    private String freeswitchHostname;
    @JsonProperty("freeswitch-ip")
    private String freeswitchIp;
    @JsonProperty("last-heartbeat-epoch")
    private String lastHeartbeatEpoch;
    @JsonProperty("socket-connected")
    private Boolean socketConnected;
    @JsonProperty("socket-authenticated")
    private Boolean socketAuthenticated;

    @JsonProperty("freeswitch-hostname")
    public String getFreeswitchHostname() {
        return freeswitchHostname;
    }

    @JsonProperty("freeswitch-hostname")
    public void setFreeswitchHostname(String freeswitchHostname) {
        this.freeswitchHostname = freeswitchHostname;
    }

    @JsonProperty("freeswitch-ip")
    public String getFreeswitchIp() {
        return freeswitchIp;
    }

    @JsonProperty("freeswitch-ip")
    public void setFreeswitchIp(String freeswitchIp) {
        this.freeswitchIp = freeswitchIp;
    }

    @JsonProperty("last-heartbeat-epoch")
    public String getLastHeartbeatEpoch() {
        return lastHeartbeatEpoch;
    }

    @JsonProperty("last-heartbeat-epoch")
    public void setLastHeartbeatEpoch(String lastHeartbeat) {
        this.lastHeartbeatEpoch = lastHeartbeat;
    }

    @JsonProperty("socket-connected")
    public Boolean getSocketConnected() {
        return socketConnected;
    }

    @JsonProperty("socket-connected")
    public void setSocketConnected(Boolean socketConnected) {
        this.socketConnected = socketConnected;
    }

    @JsonProperty("socket-authenticated")
    public Boolean getSocketAuthenticated() {
        return this.socketAuthenticated;
    }

    @JsonProperty("socket-authenticated")
    public void setSocketAuthenticated(Boolean socketAuthenticated) {
        this.socketAuthenticated = socketAuthenticated;
    }
}
