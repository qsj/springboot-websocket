package com.qsj.netty.balance.server;

import java.util.ArrayList;
import java.util.List;

public class ServerRunner {
    private static final int SERVER_QTY = 2;
    private static final String ZOOKEEPER_SERVER="192.168.12.222:2181";
    private static final String SERVER_PATH = "/servers";

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < SERVER_QTY; i++) {
            final int count = i;
            Thread thread = new Thread(()->{
                ServerData sd = new ServerData();
                sd.setBalance(0);
                sd.setHost("127.0.0.1");
                sd.setPort(10000+count);
                Server server = new ServerImpl(ZOOKEEPER_SERVER,SERVER_PATH,sd);
                server.bind();
            });
            thread.start();
//            thread.join();
            System.out.println("thread start...");
        }
    }
}
