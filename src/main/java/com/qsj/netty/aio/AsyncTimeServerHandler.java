package com.qsj.netty.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeServerHandler implements Runnable{

    CountDownLatch latch;

    AsynchronousServerSocketChannel asynchronousServerSocketChannel;
    public AsyncTimeServerHandler(int port){
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port : "+port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@Override
	public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        System.out.println("doAccept() registe the AcceptCompletionHandler!");
        try{
            latch.await();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void doAccept(){
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
    }

}