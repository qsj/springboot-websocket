package com.qsj.netty.pool;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class MyFutureTask<V> implements Future<V>,Runnable  {

    Callable<V> callable;
    boolean running = false,done=false,cancel =false;
    ReentrantLock lock;
    V outcome;
    public MyFutureTask(Callable<V> callable){
        if(callable==null){
            throw new NullPointerException("callable can not be null!");
        }
        this.callable = callable;
        this.done = false;
        this.lock = new ReentrantLock();
    }
	@Override
	public void run() {
		try{
            this.lock.lock();
            running = true;
            try {
				outcome = callable.call();
			} catch (Exception e) {
				e.printStackTrace();
            }
            done = true;
            running = false;
        }finally{
            this.lock.unlock();
        }
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
        callable = null;
        cancel = true;
		return true;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
        try{
            this.lock.lock();
            return outcome;
        }finally{
            this.lock.unlock();
        }
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		try {
            this.lock.tryLock(timeout, unit);
            return outcome;
        } catch (Exception e) {
            return null;
        }finally{
            this.lock.unlock();
        }
	}

    
}