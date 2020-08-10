package com.whut.netty;

import com.whut.netty.handler.TcpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
 * @author 杨赟
 * @describe Netty 服务端配置
 */
@Component
public class TcpServer implements ApplicationListener {

    private static final Logger LOGGER = Logger.getLogger(TcpServer.class);

    private static ServerBootstrap bootstrap = null;
    private static EventLoopGroup bossGroup = null;
    private static EventLoopGroup workerGroup = null;

    // TCP端口号
    @Value("${server.tcp.port}")
    private int PORT;

    @Autowired
    TcpHandler tcpHandler;
    // 加锁，保证只有一个线程，在启动server
    private synchronized void start() {
        if (isAccepting()) {
            LOGGER.info("TCP SERVER IS ALREADY STARTED!");
            return;
        }
        try {
            bootstrap = new ServerBootstrap();
            // netty中的reactor中的主从架构
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            // 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            // 保持长连接状态
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline p = socketChannel.pipeline();
                    // 分组接收
                    ByteBuf delimiter = Unpooled.copiedBuffer("@end".getBytes());
                    //增大缓冲区间大小20MB
                    p.addLast("package", new DelimiterBasedFrameDecoder(20 * 1024 * 1024, delimiter));
                    // 设置编解码器
                    p.addLast("encoder", new StringEncoder(StandardCharsets.UTF_8));
                    p.addLast("decoder", new StringDecoder(StandardCharsets.UTF_8));
                    //设置处理类
                    p.addLast("handler1", tcpHandler);
                }
            });
            // 服务端绑定端口
            ChannelFuture future = bootstrap.bind(PORT).sync();
            if (future.isSuccess()) {
                LOGGER.info("TCP SERVER IS STARTED!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("TCP SERVER START FAILED!");
            close();
        }
    }

    private static boolean isAccepting() {
        // server启动，并且线程池没有关闭。
        return bootstrap != null && !bossGroup.isShutdown() && !workerGroup.isShutdown();
    }

    private synchronized void close() {
        // 关闭Netty
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        // 先显式得设为null
        bootstrap = null;
        LOGGER.info("TCP SERVER CLOSED!");
        LOGGER.info("TCP IS ACCEPTING=" + isAccepting());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent && ((ContextRefreshedEvent) event).getApplicationContext().getParent() == null) {
            LOGGER.info("开启TCP服务器...");
            start();
        } else if (event instanceof ContextClosedEvent) {
            LOGGER.info("关闭TCP服务器...");
            close();
        }
    }

}
