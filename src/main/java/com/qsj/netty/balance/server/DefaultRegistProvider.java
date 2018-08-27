package com.qsj.netty.balance.server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

public class DefaultRegistProvider implements RegistProvider {
    @Override
    public void regist(Object context) throws Exception {
        ZooKeeperRegistContext registContext = (ZooKeeperRegistContext) context;
        String path = registContext.getPath();
        ZkClient zc = registContext.getZkClient();

        try {
            zc.createEphemeral(path, registContext.getData());
        } catch (ZkNoNodeException e) {
            String parentDir = path.substring(0, path.lastIndexOf('/'));
            zc.createPersistent(parentDir, true);
            regist(context);
        }
    }

    @Override
    public void unRegist(Object context) throws Exception {
        return;
    }
}
