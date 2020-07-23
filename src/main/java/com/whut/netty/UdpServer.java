package com.whut.netty;

import com.whut.netty.handler.UdpHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * UDP服务器
 */
@Component
public class UdpServer implements ApplicationListener {

    private static final Logger LOGGER = Logger.getLogger(UdpServer.class);


    private static Bootstrap bootstrap;
    private static NioEventLoopGroup bossGroup;
    // TCP端口号
    @Value("${server.udp.port}")
    private int PORT;

    @Autowired
    UdpHandler udpHandler;

    private synchronized void start() {
        if (isAccepting()) {
            LOGGER.info("UDP SERVER IS ALREADY STARTED!");
            return;
        }
        try {
            bootstrap = new Bootstrap();
            bossGroup = new NioEventLoopGroup();
            bootstrap.group(bossGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            // 分组接收
                            ByteBuf delimiter = Unpooled.copiedBuffer("@end".getBytes());
                            //增大缓冲区间大小20MB
                            p.addLast("package", new DelimiterBasedFrameDecoder(20 * 1024 * 1024, delimiter));
                            // 设置编解码器
                            p.addLast("encoder", new StringEncoder(StandardCharsets.UTF_8));
                            p.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
                            p.addLast("udpHandler", udpHandler);
                        }
                    });
            // 服务端绑定端口
            ChannelFuture future = bootstrap.bind(PORT).sync();
            if (future.isSuccess()) {
                LOGGER.info("UDP SERVER IS STARTED!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("UDP SERVER START FAILED!");
            close();
        }
    }

    private static boolean isAccepting() {
        return bootstrap != null && !bossGroup.isShutdown();
    }

    private synchronized void close() {
        // 关闭Netty
        bossGroup.shutdownGracefully();
        // 先显式得设为null
        bootstrap = null;
        LOGGER.info("UDP SERVER CLOSED!");
        LOGGER.info("UDP IS ACCEPTING=" + isAccepting());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent && ((ContextRefreshedEvent) event).getApplicationContext().getParent() == null) {
            LOGGER.info("开启UDP服务器...");
            start();
        } else if (event instanceof ContextClosedEvent) {
            LOGGER.info("关闭UDP服务器...");
            close();
        }
    }
}