package com.qsj.netty.balance.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {
    private final BalanceUpdateProvider balanceUpdateProvider;
    private static final Integer BALANCE_STEP = 1;

    public ServerHandler(BalanceUpdateProvider balanceUpdateProvider){
        this.balanceUpdateProvider = balanceUpdateProvider;
    }

    public BalanceUpdateProvider getBalanceUpdateProvider() {
        return balanceUpdateProvider;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        System.out.println("one client connect....");
        balanceUpdateProvider.addBalance(BALANCE_STEP);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        System.out.println("one client disconnect...");
        balanceUpdateProvider.reduceBalance(BALANCE_STEP);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
