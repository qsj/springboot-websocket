package com.qsj.netty.memory;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;
import sun.misc.Unsafe;

public class ThreadLocalTest {
    static int count = 100000000;
    static int times = 100000000;
    public static void main(String[] args) {
        originThreadLocalTest();
        fastThreadLocalTest();
        syncAddTest();
        casAddTest();
    }

    static void originThreadLocalTest(){
        ThreadLocal<String> t1= new ThreadLocal<>();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            t1.set("javat1");
        }
        System.out.println("threadLocal set time is "+(System.currentTimeMillis()-begin));
        begin = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            t1.get();
        }
        System.out.println("threadLocal get time is "+(System.currentTimeMillis()-begin));
    }

    static void fastThreadLocalTest(){
        new FastThreadLocalThread(new Runnable(){
        
            @Override
            public void run() {
                FastThreadLocal<String> t1 = new FastThreadLocal<>();
                long begin = System.currentTimeMillis();
                for (int i = 0; i < count; i++) {
                    t1.set("javat1");
                }
                System.out.println("fastthreadLocal set time is "+(System.currentTimeMillis()-begin));
                begin = System.currentTimeMillis();
                for (int i = 0; i < times; i++) {
                    t1.get();
                }
                System.out.println("fastthreadLocal get time is "+(System.currentTimeMillis()-begin)); 
            }
        }).start();
    }

    static Unsafe getUnsafe() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe)field.get(null);
        return unsafe;
    }

    static void testUnsafe(){
        Unsafe unsafe;
        try {
			unsafe = getUnsafe();
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
        // long offseti = unsafe.objectFieldOffset(SampleClass.class.getDeclaredField("i"));
        // System.out.println("offset of i is " + offseti);
    }

    public final class SampleClass{
        private int i = 5;
        private long l = 10;
        private byte[] buf = new byte[4];
    }

    static int threadcount = 0;
    static void syncAddTest(){
        
        Object lock = new Object();
        Thread[] threads = new Thread[10000];
        for (int i = 0; i < threads.length; i++) {
            Thread thread = new Thread(new Runnable(){
            
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        synchronized(lock){
                            threadcount++;
                        }
                    }
                }
            });
            threads[i] = thread;
        }
        long begin = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
            try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        System.out.println("sync finnaly count is "+threadcount+" time is "+(System.currentTimeMillis()-begin));
    }

    static void casAddTest(){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        Thread[] threads = new Thread[10000];
        for (int i = 0; i < threads.length; i++) {
            Thread thread = new Thread(new Runnable(){
            
                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        atomicInteger.incrementAndGet();
                    }
                }
            });
            threads[i] = thread;
        }
        long begin = System.currentTimeMillis();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
            try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        System.out.println("cas finnaly count is "+threadcount+" time is "+(System.currentTimeMillis()-begin));
    }
}