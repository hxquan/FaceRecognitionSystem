package com.whut.netty.handler;


import com.alibaba.fastjson.JSONObject;
import com.whut.api.DetectionApi;
import com.whut.domain.FatigueDetector;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by YY on 2018-03-25.
 */
@Service("TcpHandler")
@ChannelHandler.Sharable
public class TcpHandler extends SimpleChannelInboundHandler<String>
{
    private static final Logger LOGGER = Logger.getLogger(TcpHandler.class);

    private static final Map<String, FatigueDetector> videoDetectorMap = new HashMap<>();

    @Autowired
    DetectionApi detectionApi;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
    {
        if (msg != null){
            System.out.println("收到消息！" + msg.toString());
        }
        JSONObject obj = JSONObject.parseObject(msg);
        String username = obj.getString("username");
        int width = obj.getInteger("width");
        int height = obj.getInteger("height");
        String base64Image = obj.getString("base64_image");

        System.out.println(username + width + height );
        LOGGER.info(String.format("用户：%s， 图片帧（%d, %d）=%s", username, width, height, base64Image.substring(0, 100)));
        System.out.println(String.format("用户：%s， 图片帧（%d, %d）=%s", username, width, height, base64Image.substring(0, 100)));
        ctx.write(Unpooled.copiedBuffer("HTTP/1.0 200 OK\r\nContent-Type: test/html\r\n\r\n", CharsetUtil.UTF_8) );

//
//        FatigueDetector detector = videoDetectorMap.get(username);
//        if (detector == null){
//            detector = new FatigueDetector(username);
//            videoDetectorMap.put(username, detector);
//        }
//        detector.writeVideo(base64Image, width, height);
    }



    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
//        super.channelActive(ctx);
    }

    /*
     * 断开连接时，返回消息
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
//        super.channelActive(ctx);
        System.out.println("通道关闭！");
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("HTTP/1.0 200 OK\r\nContent-Type: test/html\r\n\r\n", CharsetUtil.UTF_8) );
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello netty !" , CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
