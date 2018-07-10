package com.qsj.lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ClhLock {
    /**
     * CLH锁节点状态-每个希望获取锁的线程都被封装成为一个节点对象
     */
    private static class ClhNode {
        /**
         * 默认状态为true-即处于等待状态或者加锁成功
         */
        volatile boolean active = true;
    }

    /**
     * 线程对应CLH节点
     */
    private ThreadLocal<ClhNode> currentThreadNode = new ThreadLocal<>();
    /**
     * 原子更新器
     */
    private static final AtomicReferenceFieldUpdater<ClhLock,ClhNode> UPDATER = AtomicReferenceFieldUpdater.newUpdater(ClhLock.class,
            ClhNode.class, "tail");

    /**
     * CLH加锁
     */
    public void lock() {
        ClhNode cNode = currentThreadNode.get();
        if (cNode == null) {
            cNode = new ClhNode();
            currentThreadNode.set(cNode);
        }
        ClhNode predecessor = (ClhNode) UPDATER.getAndSet(this, cNode);
        if (predecessor != null) {
            while (predecessor.active) {
            }
        }

    }

    /**
     * CLH解锁
     */
    public void unlock() {
        ClhNode cNode = currentThreadNode.get();
        if (cNode == null || !cNode.active) {
            return;
        }
        currentThreadNode.remove();
        if (!UPDATER.compareAndSet(this, cNode, null)) {
            cNode.active = false;
        }
    }

    public static void main(String[] args) {
        final ClhLock lock = new ClhLock();
        for (int i = 1; i <= 10; i++) {
            new Thread(generateTask(lock, "task" + i)).start();
        }
    }

    private static Runnable generateTask(final ClhLock lock, final String taskId) {
        return () -> {
            lock.lock();
            try {
                Thread.sleep(3000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(String.format("Thread %s Complete", taskId));
            lock.unlock();
        };
    }

}