package com.qsj.netty.aio;

public class TimeServer{
    public static void main(String[] args) {
        new Thread(new AsyncTimeServerHandler(9989)).start();
    }
}