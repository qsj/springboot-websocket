package com.qsj.lock;

import java.util.concurrent.atomic.AtomicReference;

public class SimpleSpinLock{
    private AtomicReference<Thread> ower = new AtomicReference<>();
    public void lock(){
        Thread currentThread = Thread.currentThread();
        while(!ower.compareAndSet(null, currentThread)){}
    }

    public void unlock(){
        Thread curThread = Thread.currentThread();
        ower.compareAndSet(curThread, null);
    }

    public static void main(String[] args) {
        SimpleSpinLock simpleSpinLock = new SimpleSpinLock();
        for(int i=1;i<=10;i++){
            new Thread(generateRunnable(simpleSpinLock, "taskId"+i)).start();
        }
    }

    private static Runnable generateRunnable(final SimpleSpinLock simpleSpinLock,final String taskId){
        return ()->{
            simpleSpinLock.lock();
            try{
                Thread.sleep(3000L);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            System.out.println(String.format("Thread %s Complete", taskId));
            simpleSpinLock.unlock();
        };
    }
}