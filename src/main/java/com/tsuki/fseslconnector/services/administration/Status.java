package com.tsuki.fseslconnector.services.administration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tsuki.fseslconnector.utilities.FsEslStatusStore;
import com.tsuki.fseslconnector.utilities.jsons.SokcetStatus;

@RestController
@RequestMapping("admin")
public class Status {

    private final FsEslStatusStore fsEslStatus;

    public Status(FsEslStatusStore fsEslStatus) {
        this.fsEslStatus = fsEslStatus;
    }

    @GetMapping("/status")
    public ResponseEntity<SokcetStatus> get() {
        SokcetStatus sokcetStatus = new SokcetStatus();
        sokcetStatus.setFreeswitchHostname(fsEslStatus.getFreeswitchHostname());
        sokcetStatus.setFreeswitchIp(fsEslStatus.getFreeswitchIpAddress());
        sokcetStatus.setLastHeartbeatEpoch(fsEslStatus.getLastHeartbeatEpoch());
        sokcetStatus.setSocketConnected(fsEslStatus.getIsFsEslSocketConnected().get());
        sokcetStatus.setSocketAuthenticated(fsEslStatus.getIsFsEslSocketAuthenticated().get());
        return ResponseEntity.ok().body(sokcetStatus);
    }
}
