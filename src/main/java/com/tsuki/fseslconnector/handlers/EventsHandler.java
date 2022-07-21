package com.tsuki.fseslconnector.handlers;

import static com.tsuki.fseslconnector.utilities.ProcessingConstants.CONTENT_LENGTH;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.EMPTY_LINE_MESSAGE;
import static com.tsuki.fseslconnector.utilities.ProcessingConstants.TEXT_EVENT_PLAIN;

import java.util.HashMap;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.tsuki.fseslconnector.processors.EventsProcessors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EventsHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final ThreadPoolTaskExecutor eventsProcessingPool;
    private final ByteBuf comparingBuff = Unpooled.buffer(16, 16);

    private boolean processingEvent = false;
    private boolean processingTextEventPlain = false;
    private boolean receivedEventEolHead = false;

    private final byte seperator = 58;
    private int indexOfSeperator = -1;

    private HashMap<ByteBuf, ByteBuf> event = new HashMap<>();

    public EventsHandler(ThreadPoolTaskExecutor eventsProcessingPool) {
        this.eventsProcessingPool = eventsProcessingPool;
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
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {

        if (!processingEvent) {
            if (msg.capacity() > 16) {
                msg.getBytes(0, comparingBuff);
                if (comparingBuff.compareTo(CONTENT_LENGTH) == 0) {
                    processingEvent = true;
                    comparingBuff.clear();
                    return;
                }
            }
        } else {
            if (!processingTextEventPlain) {
                if (msg.compareTo(TEXT_EVENT_PLAIN) == 0)
                    processingTextEventPlain = true;
                return;
            } else {
                if (!receivedEventEolHead) {
                    if (msg.compareTo(EMPTY_LINE_MESSAGE) == 0)
                        receivedEventEolHead = true;
                    return;
                }
                if (receivedEventEolHead) {
                    if (msg.capacity() != 0) {
                        indexOfSeperator = ByteBufUtil.indexOf(msg, 0, msg.capacity() - 1, seperator);
                        event.put(msg.copy(0, indexOfSeperator),
                                msg.copy(indexOfSeperator + 2, msg.capacity() - (indexOfSeperator + 2)));
                    } else {
                        eventsProcessingPool.execute(new EventsProcessors(new HashMap<ByteBuf, ByteBuf>(event)));
                        event.clear();
                        processingEvent = false;
                        processingTextEventPlain = false;
                        receivedEventEolHead = false;
                    }
                }
            }
        }
    }
}
