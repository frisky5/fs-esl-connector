package com.tsuki.fseslconnector.processors;

import static com.tsuki.fseslconnector.utilities.fseslContants.EventKeys.EVENT_DATE_TIMESTAMP;
import static com.tsuki.fseslconnector.utilities.fseslContants.EventKeys.EVENT_NAME;
import static com.tsuki.fseslconnector.utilities.fseslContants.EventKeys.FREESWITCH_HOSTNAME;
import static com.tsuki.fseslconnector.utilities.fseslContants.EventKeys.FREESWITCH_IPV4;
import static com.tsuki.fseslconnector.utilities.fseslContants.EventsValues.HEARTBEAT;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsuki.fseslconnector.utilities.FsEslStatusStore;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class EventsProcessors implements Runnable {
    private final Logger LOG = LoggerFactory.getLogger(EventsProcessors.class);
    private final HashMap<ByteBuf, ByteBuf> event;
    private final FsEslStatusStore fsEslStatusStore;

    public EventsProcessors(HashMap<ByteBuf, ByteBuf> event, FsEslStatusStore fsEslStatusStore) {
        this.event = event;
        this.fsEslStatusStore = fsEslStatusStore;
    }

    @Override
    public void run() {
        if (event.get(EVENT_NAME).compareTo(HEARTBEAT) == 0) {
            fsEslStatusStore.setLastHeartbeatEpoch(event.get(EVENT_DATE_TIMESTAMP).toString(CharsetUtil.UTF_8));

            fsEslStatusStore.setFreeswitchIpAddress(event.get(FREESWITCH_IPV4).toString(CharsetUtil.UTF_8));
            fsEslStatusStore.setFreeswitchHostname(event.get(FREESWITCH_HOSTNAME).toString(CharsetUtil.UTF_8));
            LOG.info("Event Epoch : " + event.get(EVENT_DATE_TIMESTAMP).toString(CharsetUtil.UTF_8));
            Instant instant = Instant
                    .ofEpochMilli(
                            Long.parseLong(event.get(EVENT_DATE_TIMESTAMP).toString(CharsetUtil.UTF_8)) / 1000);
            LOG.info("Event Time : " + instant);
            // event.keySet().forEach(
            // key -> {
            // LOG.info(key.toString(CharsetUtil.US_ASCII) + ":"
            // + event.get(key).toString(CharsetUtil.US_ASCII));
            // });
        }
        /*
         * Always release ByteBuffs of the HashMap after processing the event to avoid
         * memory leaks in NettyIO
         */
        event.keySet().forEach(
                key -> {
                    event.get(key).release();
                    key.release();
                });
    }
}
