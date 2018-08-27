package com.qsj.netty.balance.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.StampedLock;

public class ServerImpl implements Server {
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private  EventLoopGroup workerGroup = new NioEventLoopGroup();
    private ServerBootstrap bootstrap = new ServerBootstrap();
    private ChannelFuture channelFuture;
    private  String zkAddress;
    private  String serverPath;
    private String currentPath;
    private  ServerData sd;

    private volatile  boolean binded = false;
    private final ZkClient zkClient;
    private final RegistProvider registProvider;

    private static final Integer SESSION_TIME_OUT = 10000;
    private static final Integer CONNECT_TIME_OUT = 10000;

    public ServerImpl(String zkAddress, String serversPath, ServerData sd){
        this.zkAddress = zkAddress;
        this.serverPath = serversPath;
        this.sd = sd;
        this.zkClient = new ZkClient(this.zkAddress,SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());
        this.registProvider = new DefaultRegistProvider();
        Executors.newFixedThreadPool(1);
        Map<String,String> map = new WeakHashMap<>();
        StampedLock stampedLock = new StampedLock();
        LongAdder longAdder = new LongAdder();
    }

    private void  initRunning()throws  Exception{
        String mePath = serverPath.concat("/").concat(sd.getPort()+"").toString();
        registProvider.regist(new ZooKeeperRegistContext(mePath,zkClient,sd));
    }

    @Override
    public void bind() {
        if(binded){
            return;
        }
        System.out.println("port "+sd.getPort()+"is running...");

        try {
            initRunning();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline ch = socketChannel.pipeline();
                        ch.addLast(new ServerHandler(new DefaultBalanceUpdateProvider(serverPath.concat("/").concat(sd.getPort()+""),zkClient)));
                    }
                });
        try{
            ChannelFuture cf= bootstrap.bind(sd.getPort()).sync();
            binded = true;
            System.out.println("port "+sd.getPort()+" binded...");
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
