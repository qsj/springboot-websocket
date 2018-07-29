package com.qsj.netty.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

public class TestPool {

    public static void main(String[] args) {
        // testThreadPool();
        testNettyLoop();
    }

    public static void testNettyLoop(){
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        loopGroup.next().submit(new RunJob());
        loopGroup.next().execute(new RunJob());
        loopGroup.execute(new RunJob());
    }

    public static void testThreadPool(){
// CommonThreadPool<RunJob> commonThreadPool = new CommonThreadPool<>();
        // commonThreadPool.addWorkers(50);
        // for (int i = 0; i < 50; i++) {
        //     commonThreadPool.execute(new RunJob());
        // }

        // ExecutorService fixedThreadService = Executors.newFixedThreadPool(10);
        // for (int i = 0; i < 60; i++) {
        //     fixedThreadService.execute(new RunJob());
        // }

        // ExecutorService singleThreadService = Executors.newSingleThreadExecutor();
        // for (int i = 0; i < 60; i++) {
        //     singleThreadService.execute(new RunJob());
        // }
        
        // ExecutorService cachedThreadService = Executors.newCachedThreadPool();
        // for (int i = 0; i < 60; i++) {
        //     cachedThreadService.execute(new RunJob());
        // }

        ScheduledExecutorService scheduleService = Executors.newScheduledThreadPool(5);
        scheduleService.execute(new RunJob());
        scheduleService.schedule(new RunJob(), 5, TimeUnit.SECONDS);
        scheduleService.scheduleAtFixedRate(new RunJob(), 5, 5, TimeUnit.SECONDS);
    }


    static class RunJob implements Runnable {

		@Override
		public void run() {
			try {
                //Thread.sleep(3000L);
                System.out.println(Thread.currentThread().getName()+"run over");
            } catch (Exception e) {
                //TODO: handle exception
            }
		}
    }
}