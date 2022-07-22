package com.tsuki.fseslconnector;

import static com.tsuki.fseslconnector.utilities.fseslContants.ProcessingConstants.AUTHENTICATION_HANDLER;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsuki.fseslconnector.configuration.FsEslClientProperties;
import com.tsuki.fseslconnector.handlers.AuthenticationAndSubscribeHandler;
import com.tsuki.fseslconnector.utilities.FsEslStatusStore;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.util.CharsetUtil;

public class FsEslClient implements Runnable {
    private final Logger LOG = LoggerFactory.getLogger(FsEslClient.class);

    private final AuthenticationAndSubscribeHandler authenticationHandler;
    private final FsEslClientProperties fsEslClientProperties;
    private final FsEslStatusStore fsEslStatusStore;

    private EventLoopGroup group;
    private Bootstrap clientBootstrap;
    private ChannelFuture channelFuture;

    public FsEslClient(
            AuthenticationAndSubscribeHandler authenticationHandler,
            FsEslClientProperties fsEslClientProperties,
            FsEslStatusStore fsEslStatusStore) {
        this.authenticationHandler = authenticationHandler;
        this.fsEslClientProperties = fsEslClientProperties;
        this.fsEslStatusStore = fsEslStatusStore;
        new Thread(this).start();
    }

    public void send(String message) {
        if (channelFuture.channel().isWritable()) {
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
        }
    }

    @Override
    public void run() {
        group = new NioEventLoopGroup();
        clientBootstrap = new Bootstrap();
        try {
            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(
                    new InetSocketAddress(fsEslClientProperties.getIp(), fsEslClientProperties.getPort()));

            clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LineBasedFrameDecoder(Integer.MAX_VALUE, true, false));
                    socketChannel.pipeline().addLast(AUTHENTICATION_HANDLER, authenticationHandler);
                }
            });

            try {
                LOG.info("Connecting (TCP) to Freeswitch mod_event_socket module on IP : "
                        + fsEslClientProperties.getIp()
                        + ":" + fsEslClientProperties.getPort());
                channelFuture = clientBootstrap.connect().sync();
                LOG.info("TCP Connection is UP, updating socket status store.");
                fsEslStatusStore.setIsFsEslSocketConnected(true);
            } catch (InterruptedException e) {
                LOG.error("FS ESL Socket Connect failed", e);
                return;
            }

            try {
                LOG.info("Registering TCP Connection Close Future.");
                channelFuture.channel().closeFuture().sync();
                LOG.info("TCP Connection is Down, updating socket status store.");
                fsEslStatusStore.setIsFsEslSocketConnected(true);
            } catch (InterruptedException e) {
                LOG.error("FS ESL Socket Closed", e);
            }
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LOG.error("FS ESL Socket Graceful shutdown failed", e);
            }
        }

    }
}
