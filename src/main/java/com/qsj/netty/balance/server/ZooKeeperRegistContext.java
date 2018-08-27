package com.qsj.netty.balance.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.I0Itec.zkclient.ZkClient;

@Data
@AllArgsConstructor
public class ZooKeeperRegistContext {
    private String path;
    private ZkClient zkClient;
    private Object data;
}
