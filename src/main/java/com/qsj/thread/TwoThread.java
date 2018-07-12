package com.qsj.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TwoThread {
    private static volatile int num = 0;
    private static Lock lock = new ReentrantLock();
    private static Condition oddCondition = lock.newCondition();
    private static Condition evenCondition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        OddEvenPrint oddevenprint = new OddEvenPrint();
        new Thread(() -> {
            try {
                oddevenprint.print(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                oddevenprint.print(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static class OddEvenPrint {
        public void print(boolean numFlag) throws InterruptedException {
            while (num <= 100) {
                lock.lock();
                if (numFlag) {
                    if(num!=0){
                        System.out.println(Thread.currentThread().getName()+" odd is " + num);
                    }
                    evenCondition.signal();
                    oddCondition.await();
                } else {
                    if(num!=0) {
                        System.out.println(Thread.currentThread().getName()+" even is " + num);
                    }
                    oddCondition.signal();
                    evenCondition.await();
                }
                num++;
                lock.unlock();
            }
        }
    }

}
