package com.tsuki.fseslconnector;

import static com.tsuki.fseslconnector.utilities.ProcessingConstants.AUTHENTICATION_HANDLER;

import java.net.InetSocketAddress;

import com.tsuki.fseslconnector.configuration.FsEslClientProperties;
import com.tsuki.fseslconnector.handlers.AuthenticationAndSubscribeHandler;

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

    private final AuthenticationAndSubscribeHandler authenticationHandler;
    private final FsEslClientProperties fsEslClientProperties;
    private EventLoopGroup group;
    private Bootstrap clientBootstrap;
    private ChannelFuture channelFuture;

    public FsEslClient(AuthenticationAndSubscribeHandler authenticationHandler,
            FsEslClientProperties fsEslClientProperties) {
        this.authenticationHandler = authenticationHandler;
        this.fsEslClientProperties = fsEslClientProperties;
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
                channelFuture = clientBootstrap.connect().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
