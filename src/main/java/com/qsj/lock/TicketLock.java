package com.qsj.lock;

import java.util.concurrent.atomic.AtomicInteger;

public class TicketLock {
    private AtomicInteger serviceNum = new AtomicInteger(0);

    private AtomicInteger ticketNum = new AtomicInteger(0);

    public int lock() {
        int acquireTicketNum = ticketNum.getAndIncrement();

        while (acquireTicketNum != serviceNum.get()) {
        }
        return acquireTicketNum;
    }

    public void unlock(int ticketNum) {
        int nextServiceNum = serviceNum.get() + 1;
        serviceNum.compareAndSet(ticketNum, nextServiceNum);
    }

    public static void main(String[] args) {
        TicketLock ticketLock = new TicketLock();
        for (int i = 1; i <= 10; i++) {
            new Thread(generateRunnable(ticketLock, "taskId" + i)).start();
        }
    }

    private static Runnable generateRunnable(final TicketLock ticketLock, final String taskId) {
        return () -> {
            int i = ticketLock.lock();
            try {
                Thread.sleep(3000L);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println(String.format("Thread %s Complete,ticket num is %d", taskId, i));
            ticketLock.unlock(i);
        };
    }
}