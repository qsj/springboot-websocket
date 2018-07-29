package com.qsj.netty.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

import org.apache.commons.lang.StringUtils;


public class ReadCompletionHandler implements CompletionHandler<Integer,ByteBuffer>{

    private AsynchronousSocketChannel channel;
    public ReadCompletionHandler(AsynchronousSocketChannel channel){
        if(this.channel==null){
            this.channel = channel;
        }
    }

	@Override
	public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        try {
            String req = new String(body,"UTF-8");
            System.out.println("The time server receive order : "+req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
            System.out.println("The time server response order :"+currentTime);
            doWrite(currentTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void doWrite(String currentTime){
        if(StringUtils.isNotBlank(currentTime)){
            ByteBuffer writeByteBuffer = ByteBuffer.allocate(currentTime.getBytes().length);
            writeByteBuffer.put(currentTime.getBytes());
            writeByteBuffer.flip();
            channel.write(writeByteBuffer,writeByteBuffer,new CompletionHandler<Integer,ByteBuffer>() {
                @Override
				public void completed(Integer result, ByteBuffer buffer) {
					if(buffer.hasRemaining()){//写完成
						channel.write(buffer,buffer,this);
					}
				}

				@Override
				public void failed(Throwable exc, ByteBuffer attachment) {
					try {
						channel.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
            });
        }
    }

	@Override
	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
            this.channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

}