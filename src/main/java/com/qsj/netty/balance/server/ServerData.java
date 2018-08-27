package com.qsj.netty.balance.server;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServerData implements Serializable,Comparable<ServerData> {
    private  Integer balance;
    private String host;
    private  int port;
    @Override
    public int compareTo(ServerData o) {
        return this.getBalance().compareTo(o.getBalance());
    }

    @Override
    public String toString(){
        return String.format("ServerData[balance=%s,host=%s,port=%s]",balance,host,port);
    }
}
