package com.tsuki.fseslconnector.handlers;

import static com.tsuki.fseslconnector.utilities.ProcessingConstants.AUTHENTICATION_HANDLER;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.AUTH_REQUEST_MESSAGE;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.COMMAND_REPLY;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.EMPTY_LINE_MESSAGE;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.EVENTS_HANDLER;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.AUTH_REPLY_ERR;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.AUTH_REPLY_OK;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.EVENT_SUBSCRIBE_REPLY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsuki.fseslconnector.configuration.FsEslClientProperties;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class AuthenticationAndSubscribeHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final Logger LOG = LoggerFactory.getLogger(AuthenticationAndSubscribeHandler.class);
    private final EventsHandler eventsHandler;
    private final FsEslClientProperties fsEslClientProperties;
    private final String eventsSubscribeCommand;

    boolean receivedAuthRequest = false;
    boolean receivedAuthRequestEol = false;
    boolean sentAuthResponse = false;
    boolean receivedAuthReply = false;
    boolean receivedAuthReplyStatus = false;
    boolean receivedAuthReplyEol = false;
    boolean isAuthetnticated = false;

    boolean receivedEventSubscribeReply = false;
    boolean receivedEventSubscribeValidation = false;

    boolean sentEventsJsonSubcribe = false;

    public AuthenticationAndSubscribeHandler(EventsHandler eventsHandler, FsEslClientProperties fsEslClientProperties) {
        this.eventsHandler = eventsHandler;
        this.fsEslClientProperties = fsEslClientProperties;
        eventsSubscribeCommand = "events plain " + fsEslClientProperties.getEvents() + "\n\n";
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) {
        cause.printStackTrace();
        channelHandlerContext.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf message) {
        // LOG.info(message.getCharSequence(0, message.readableBytes(),
        // CharsetUtil.UTF_8).toString());

        if (!receivedAuthRequest
                && message.compareTo(AUTH_REQUEST_MESSAGE) == 0) {
            LOG.info("Received Authentication Request from Freeswtich.");
            receivedAuthRequest = true;
            return;
        }
        if (receivedAuthRequest
                && !receivedAuthRequestEol
                && message.compareTo(EMPTY_LINE_MESSAGE) == 0) {
            receivedAuthRequestEol = true;
            LOG.info("Sending Authentication response to Freeswitch.");
            ctx.writeAndFlush(
                    Unpooled.copiedBuffer("auth " + fsEslClientProperties.getPassword() + "\n\n",
                            CharsetUtil.UTF_8));
            sentAuthResponse = true;
            return;
        }

        if (receivedAuthRequest
                && receivedAuthRequestEol
                && sentAuthResponse
                && !receivedAuthReply
                && message.compareTo(COMMAND_REPLY) == 0) {
            LOG.info("Received Authentication Command Reply from Freeswitch.");
            receivedAuthReply = true;
            return;
        }
        if (receivedAuthRequest
                && receivedAuthRequestEol
                && sentAuthResponse
                && receivedAuthReply
                && !isAuthetnticated
                && message.compareTo(AUTH_REPLY_OK) == 0) {
            isAuthetnticated = true;
            return;
        }
        if (receivedAuthRequest
                && receivedAuthRequestEol
                && sentAuthResponse
                && receivedAuthReply
                && !isAuthetnticated
                && message.compareTo(AUTH_REPLY_ERR) == 0) {
            LOG.info("Received Authentication Command Reply Status from Freeswitch, Authentication FAILED.");
            return;
        }
        if (isAuthetnticated && !receivedAuthReplyEol && message.compareTo(EMPTY_LINE_MESSAGE) == 0) {
            receivedAuthReplyEol = true;
            LOG.info(
                    "FS ESL Socket is authenticated, sending event subscribe command.");
            ctx.writeAndFlush(Unpooled.copiedBuffer(eventsSubscribeCommand, CharsetUtil.UTF_8));
            return;
        }
        if (isAuthetnticated && !receivedEventSubscribeReply && message.compareTo(COMMAND_REPLY) == 0) {
            receivedEventSubscribeReply = true;
            return;
        }
        if (isAuthetnticated && receivedEventSubscribeReply && message.compareTo(EVENT_SUBSCRIBE_REPLY) == 0) {
            receivedEventSubscribeValidation = true;
            return;
        }
        if (isAuthetnticated && receivedEventSubscribeReply && receivedEventSubscribeValidation
                && message.compareTo(EMPTY_LINE_MESSAGE) == 0) {
            LOG.info(
                    "FS ESL Socket is authenticated, and subscribed to events, removing auth handler and adding events handler");
            ctx.pipeline().addLast(EVENTS_HANDLER, eventsHandler);
            ctx.pipeline().remove(AUTHENTICATION_HANDLER);
            return;
        }
    }
}
