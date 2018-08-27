package com.qsj.netty.balance.client;

import com.qsj.netty.balance.server.ServerData;

import java.util.ArrayList;
import java.util.List;

public class ClientRunner {
    private static final int CLIENT_QTY = 3;
    private static final String ZOOKEEPER_SERVER="192.168.12.222:2181";
    private static final String SERVER_PATH = "/servers";

    public static void main(String[] args) throws InterruptedException {
        final List<Client> clients = new ArrayList<>();
        final BalanceProvider<ServerData> balanceProvider = new DefaultBalanceProvider(ZOOKEEPER_SERVER,SERVER_PATH);

        for (int i = 0; i < CLIENT_QTY; i++) {
            Thread thread = new Thread(()->{
                Client client = new Client(balanceProvider);
                clients.add(client);
                client.connect();
            });
            thread.start();
            thread.join();
            Thread.sleep(200L);
        }
        for (int i = 0; i < CLIENT_QTY; i++) {
            clients.get(i).disconnect();
        }
    }
}
