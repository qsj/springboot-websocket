package com.qsj.netty.balance.client;

import com.qsj.netty.balance.server.ServerData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    private final BalanceProvider<ServerData> balanceProvider;
    private EventLoopGroup eventLoopGroup = null;
    private Channel channel = null;

    public Client(BalanceProvider<ServerData> balanceProvider){
        this.balanceProvider = balanceProvider;
    }

    public BalanceProvider<ServerData> getBalanceProvider() {
        return balanceProvider;
    }

    public void connect(){
            try {
                ServerData serverData = balanceProvider.getBalanceItem();
                System.out.println("connecting to "+serverData.getHost()+":"+serverData.getPort()+", it's balance:"+serverData.getBalance());
                eventLoopGroup = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(eventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new ClientHandler());
                            }
                        });
                ChannelFuture future = bootstrap.connect(serverData.getHost(),serverData.getPort()).syncUninterruptibly();
                channel = future.channel();
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    public void disconnect(){
        try {
            if(channel!=null){
                channel.close().syncUninterruptibly();
            }
            eventLoopGroup.shutdownGracefully();
            eventLoopGroup = null;
            System.out.println("disconnect");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
