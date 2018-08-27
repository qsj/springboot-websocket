package com.qsj.netty.balance.client;

import com.qsj.netty.balance.server.ServerData;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData> {
    private final String zkServer;
    private final String serverPath;
    private final ZkClient zkClient;

    private static final Integer SESSION_TIMEOUT=10000;
    private static final Integer CONNECT_TIMEOUT=10000;

    public DefaultBalanceProvider(String zkServer,String serverPath) {
        this.zkServer = zkServer;
        this.serverPath = serverPath;
        this.zkClient = new ZkClient(this.zkServer,SESSION_TIMEOUT,CONNECT_TIMEOUT,new SerializableSerializer());
    }

    @Override
    protected ServerData balanceAlgorithm(List<ServerData> items) {
        if(items.size()>0){
            Collections.sort(items);
            return items.get(0);
        }
        return null;
    }

    @Override
    protected List<ServerData> getBalanceItems() {
        List<ServerData> sdList = new ArrayList<>();
        List<String> children = zkClient.getChildren(this.serverPath);
        for (int i = 0; i <children.size() ; i++) {
            String path = serverPath.concat("/").concat(children.get(i));
            System.out.println(path);
            sdList.add(zkClient.readData(path));
        }
        return sdList;
    }
}
