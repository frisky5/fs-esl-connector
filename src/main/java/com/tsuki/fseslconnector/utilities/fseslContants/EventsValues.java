package com.tsuki.fseslconnector.utilities.fseslContants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public interface EventsValues {
    /*
     * This interface contains the values that might be found in a key with the name
     * Event-Name in the event HashMap in the Executor runnable. the reason for
     * using ByteBuf if to avoid String conversion as much as possible in the code
     */
    ByteBuf CUSTOM = Unpooled.copiedBuffer("CUSTOM", CharsetUtil.UTF_8);
    ByteBuf HEARTBEAT = Unpooled.copiedBuffer("HEARTBEAT", CharsetUtil.UTF_8);
}
