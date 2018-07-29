package com.qsj.netty.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements Runnable,CompletionHandler<Void,AsyncTimeClientHandler>{
    private String host;
    private int port;
    CountDownLatch latch;
    AsynchronousSocketChannel channel;

    public AsyncTimeClientHandler(String host,int port){
        this.host = host;
        this.port = port;
        try {
            this.channel = AsynchronousSocketChannel.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	public void completed(Void result, AsyncTimeClientHandler attachment) {
        String req = "QUERY TIME ORDE";
        byte[] reqBytes = req.getBytes(); 
        ByteBuffer buffer = ByteBuffer.allocate(reqBytes.length);
        buffer.put(reqBytes);
        buffer.flip();
        channel.write(buffer,buffer,new CompletionHandler<Integer,ByteBuffer>() {
			@Override
			public void completed(Integer result, ByteBuffer attachmentBuffer) {
                if(attachmentBuffer.hasRemaining()){
                    channel.write(attachmentBuffer,attachmentBuffer,this);
                }else{
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    channel.read(readBuffer,readBuffer,new CompletionHandler<Integer,ByteBuffer>() {

						@Override
						public void completed(Integer result, ByteBuffer attachment) {
							attachment.flip();
                            byte[] resbytes = new byte[attachment.remaining()];
                            attachment.get(resbytes);
                            String resbody = null;
                            try {
                                resbody = new String(resbytes,"UTF-8");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("Now is:"+resbody);
                            latch.countDown();
						}

						@Override
						public void failed(Throwable exc, ByteBuffer attachment) {
                            exc.printStackTrace();
                            try {
                                channel.close();
                                latch.countDown();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
						}

                    });
                }
                
			}

			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
                try {
					channel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
                latch.countDown();
			}
        });
	}

	@Override
	public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
		try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        latch.countDown();
	}

	@Override
	public void run() {
        latch = new CountDownLatch(1);
        channel.connect(new InetSocketAddress(host, port),this,this);
        try {
            latch.await();
            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}