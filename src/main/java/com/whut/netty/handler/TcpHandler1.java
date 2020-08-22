package com.whut.netty.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service("TcpHandler1")
@Sharable
public class TcpHandler1 extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = Logger.getLogger(TcpHandler1.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        super.channelRead(ctx, msg);
//        ByteBuf in = (ByteBuf) msg   ;



        JSONObject obj = JSONObject.parseObject((String) msg);
        String username = obj.getString("username");
        int width = obj.getInteger("width");
        int height = obj.getInteger("height");
        String base64Image = obj.getString("base64_image");
        LOGGER.info(String.format("用户：%s， 图片帧（%d, %d）=%s", username, width, height, base64Image.substring(0, 100)));


//        System.out.println("server received : " + in.toString(CharsetUtil.UTF_8 ));
        ctx.write(Unpooled.copiedBuffer("HTTP/1.0 200 OK\r\nContent-Type: test/html\r\n\r\n", CharsetUtil.UTF_8) );
//        ctx.write(in);

        ctx.write(username + width + height + base64Image.substring(0,20));

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(this.getClass().getSimpleName() + "  注册到ChannelPipe！");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接的客户端地址: " + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("地址为  " + ctx.channel().remoteAddress()+ "  客户端的连接断开!" );
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        super.channelReadComplete(ctx);
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();


    }



}
