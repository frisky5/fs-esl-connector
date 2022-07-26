package com.tsuki.fseslconnector.utilities.fseslContants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public interface EventKeys {
    /*
     * This Interface contains the keys values that might be found in the event
     * HashMap in the Runnable of the Executor, using ByteBuf to avoid String
     * conversion as much as possible
     */
    ByteBuf EVENT_NAME = Unpooled.copiedBuffer("Event-Name", CharsetUtil.UTF_8);
    ByteBuf EVENT_DATE_TIMESTAMP = Unpooled.copiedBuffer("Event-Date-Timestamp", CharsetUtil.UTF_8);
    ByteBuf FREESWITCH_HOSTNAME = Unpooled.copiedBuffer("FreeSWITCH-Hostname", CharsetUtil.UTF_8);
    ByteBuf FREESWITCH_IPV4 = Unpooled.copiedBuffer("FreeSWITCH-IPv4", CharsetUtil.UTF_8);
    ByteBuf FREESWITCH_SWITCHNAME = Unpooled.copiedBuffer("FreeSWITCH-Switchname", CharsetUtil.UTF_8);
}
