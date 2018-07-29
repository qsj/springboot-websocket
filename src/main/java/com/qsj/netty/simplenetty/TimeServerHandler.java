package com.qsj.netty.simplenetty;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg)throws Exception{
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String reqStr = new String(req, "UTF-8");
        System.out.println("The time server receive order is:"+reqStr);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(reqStr)?new Date().toString():"BAD REQ";
        ByteBuf wBuf = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(wBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)throws Exception{
        ctx.flush();
    }
}