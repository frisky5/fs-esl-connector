package com.tsuki.fseslconnector.processors;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;

public class EventsProcessors implements Runnable {
    private final Logger LOG = LoggerFactory.getLogger(EventsProcessors.class);
    private final HashMap<ByteBuf, ByteBuf> event;

    public EventsProcessors(HashMap<ByteBuf, ByteBuf> event) {
        this.event = event;
    }

    @Override
    public void run() {
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
