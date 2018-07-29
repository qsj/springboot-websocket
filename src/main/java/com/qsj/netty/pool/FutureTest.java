package com.qsj.netty.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

public class FutureTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTest futureTest = new FutureTest();
        Future<String> future = futureTest.searchByMyFuture("my test");
        System.out.println("result is "+future.get());

        Promise<String> promise = futureTest.searchPromise("netty in action");
        // String result = promise.get();
        // System.out.println("result is "+result);
        promise.addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super String>>() {

			@Override
			public void operationComplete(io.netty.util.concurrent.Future<? super String> future) throws Exception {
				System.out.println("listener 1 email to one,price is "+future.get());
			}
        });
        promise.addListener(new GenericFutureListener<io.netty.util.concurrent.Future<? super String>>() {

			@Override
			public void operationComplete(io.netty.util.concurrent.Future<? super String> future) throws Exception {
				System.out.println("listener 2 email to two,price is "+future.get());
			}
        });
    }

    public Future<String> search(String prodName){
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception {
                try {
                    System.out.println(">>Search "+prodName+" from internet!");
                    Thread.sleep(1000L);
                    return "$18.1";
                } catch (Exception e) {
                    e.printStackTrace();
                }
				return null;
			}
        });
        new Thread(futureTask).start();
        return futureTask;
    }

    public Future<String> searchByMyFuture(String prodName){
        MyFutureTask<String> futureTask = new MyFutureTask<>(new Callable<String>() {

			@Override
			public String call() throws Exception {
				try {
                    System.out.println(">>Search "+prodName+" from internet!");
                    Thread.sleep(1000L);
                    return "$18.1";
                } catch (Exception e) {
                    //TODO: handle exception
                }
				return null;
			}
        });
        new Thread(futureTask).start();
        return futureTask;
    }

    public Promise<String> searchPromise(String prodName){
        EventLoopGroup loop = new NioEventLoopGroup();
        DefaultPromise<String> promise = new DefaultPromise<>(loop.next());
        loop.schedule(new Runnable(){
            @Override
            public void run() {
                try {
                    System.out.println(">>Search "+prodName+" from internet!");
                    Thread.sleep(1000L);
                    promise.setSuccess("$9.99");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, TimeUnit.SECONDS);
        return promise;
    }
}