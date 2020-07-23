package com.whut.netty.handler;

import com.whut.util.ImageUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class UdpHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Logger logger = Logger.getLogger(UdpHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        // 接受client的消息
        String msgString = msg.content().toString(CharsetUtil.UTF_8);
        if (msgString.endsWith("@end")){
            int index = msgString.indexOf("@");
            if (index > 0){
                String username = msgString.substring(0, index);
                String base64 = msgString.substring(index+1, msgString.length() - 4);
                ImageUtil.base64ToImage(base64, String.format("/Users/yy/Downloads/图片/%s.jpg", username));
            }
        }

        logger.info(msgString);
        if (msgString.contains("UdpServer")) {
            ctx.writeAndFlush(new DatagramPacket(Unpooled.wrappedBuffer("helloClient".getBytes()), msg.sender()));

        }
    }

}