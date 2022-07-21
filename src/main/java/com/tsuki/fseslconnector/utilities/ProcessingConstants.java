package com.tsuki.fseslconnector.utilities;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public interface ProcessingConstants {
    String AUTHENTICATION_HANDLER = "AUTHENTICATION_HANDLER";
    String EVENTS_HANDLER = "EVENTS_HANDLER";

    ByteBuf EMPTY_LINE_MESSAGE = Unpooled.copiedBuffer("", CharsetUtil.UTF_8);

    ByteBuf AUTH_REQUEST_MESSAGE = Unpooled.copiedBuffer("Content-Type: auth/request", CharsetUtil.UTF_8);
    ByteBuf AUTH_REPLY_OK = Unpooled.copiedBuffer("Reply-Text: +OK accepted", CharsetUtil.UTF_8);
    ByteBuf AUTH_REPLY_ERR = Unpooled.copiedBuffer("Reply-Text: -ERR invalid", CharsetUtil.UTF_8);

    ByteBuf COMMAND_REPLY = Unpooled.copiedBuffer("Content-Type: command/reply", CharsetUtil.UTF_8);

    ByteBuf CONTENT_LENGTH = Unpooled.copiedBuffer("Content-Length: ", CharsetUtil.UTF_8);
    ByteBuf TEXT_EVENT_PLAIN = Unpooled.copiedBuffer("Content-Type: text/event-plain", CharsetUtil.UTF_8);
    ByteBuf EVENT_SUBSCRIBE_REPLY = Unpooled.copiedBuffer("Reply-Text: +OK event listener enabled plain",
            CharsetUtil.UTF_8);
}
