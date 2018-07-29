package com.qsj.netty.memory;

import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator.Handle;

public class AdaptiveRecvByteBufAllocatorTest {

    public static void main(String[] args) {
        AdaptiveRecvByteBufAllocator adAllocator = new AdaptiveRecvByteBufAllocator();
        Handle handle = adAllocator.newHandle();
        System.out.println("read cycle 1");
        handle.reset(null);
        System.out.println("read cycle 1-1 guess:"+handle.guess());
        handle.lastBytesRead(1024);
        System.out.println("read cycle 1-2 guess:"+handle.guess());
        handle.lastBytesRead(1024);
        handle.readComplete();

        System.out.println("read cycle 2");
        handle.reset(null);
        System.out.println("read cycle 2-1 guess:"+handle.guess());
        handle.lastBytesRead(1024);
        handle.readComplete();

        System.out.println("read cycle 3");
        handle.reset(null);
        System.out.println("read cycle 3-1 guess:"+handle.guess());
        handle.lastBytesRead(1024);
        handle.readComplete();

        System.out.println("read cycle 4");
        handle.reset(null);
        System.out.println("read cycle 4-1 need:"+handle.guess());
        handle.readComplete();
    }
}