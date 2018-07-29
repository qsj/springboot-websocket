package com.qsj.netty.pool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CommonThreadPool<Job extends Runnable> implements ThreadPool<Job> {

    private static final int MAX_WORKER_NUMS = 60;
    private static final int DEFAULT_WORKER_NUMS = 1;
    private final LinkedList<Job> jobs = new LinkedList<>();
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong threadNum = new AtomicLong();

    public CommonThreadPool(){
        initializeWorkers(DEFAULT_WORKER_NUMS);
    }
    private void initializeWorkers(int num){
        for (int i = 0; i < num; i++) {
            Worker worker = new Worker();
            workers.add(worker);
            String threadName = "ThreadPool-worker-"+threadNum.getAndIncrement();
            Thread thread = new Thread(worker,threadName);
            thread.start();
            System.out.println(threadName+" started");
        }
    }
	@Override
	public void execute(Job job) {
		if(job==null){
            return;
        }
        synchronized(jobs){
            jobs.addLast(job);
            jobs.notify();
        }
	}

	@Override
	public void shutdown() {
		for (Worker worker : workers) {
            worker.shutdown();
        }
	}

	@Override
	public void addWorkers(int num) {
		synchronized(jobs){
            int size = workers.size();
            if(num+size > MAX_WORKER_NUMS){
                num = MAX_WORKER_NUMS - size;
            }
            initializeWorkers(num);
        }
	}

	@Override
	public void removeWorker(int num) {
		synchronized(jobs){
            if(num>=this.workers.size()){
                throw new IllegalArgumentException("beyond workNum");
            }
            int count =0;
            while(count < num){
                Worker worker = workers.get(count);
                if(workers.remove(worker)){
                    worker.shutdown();
                    count++;
                }
            }
                
        }
	}

	@Override
	public int getJobSize() {
		return jobs.size();
	}

    /**
     * InnerCommonThreadPool
     */
    public class Worker implements Runnable {
        private volatile boolean runing = true;
		@Override
		public void run() {
			while(runing){
                Job job = null;
                synchronized(jobs){
                    while(jobs.isEmpty()){
                        try {
                            jobs.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    job = jobs.removeFirst();
                    if(job!=null){
                        try{
                            job.run();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
		}
    
        public void shutdown(){
            runing = false;
        }
    }
    
}