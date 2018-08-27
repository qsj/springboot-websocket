package com.qsj.netty.balance.server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkBadVersionException;
import org.apache.zookeeper.data.Stat;

public class DefaultBalanceUpdateProvider implements BalanceUpdateProvider {
    private String serverPath;
    private ZkClient zkClient;

    public DefaultBalanceUpdateProvider(String serverPath,ZkClient zkClient){
        this.serverPath = serverPath;
        this.zkClient = zkClient;
    }

    @Override
    public boolean addBalance(int step) {
        Stat stat = new Stat();
        ServerData sd;
        while (true){
            try {
                sd = zkClient.readData(this.serverPath,stat);
                sd.setBalance(sd.getBalance()+step);
                zkClient.writeData(this.serverPath,sd,stat.getVersion());
                return true;
            }catch (ZkBadVersionException e1){
                //ignore
            }catch (Exception e){
                e.printStackTrace();
                return  false;
            }
        }
    }

    @Override
    public boolean reduceBalance(int step) {
        Stat stat = new Stat();
        ServerData sd;
        while (true){
            try {
                sd = zkClient.readData(this.serverPath,stat);
                final Integer currBalance = sd.getBalance();
                sd.setBalance(currBalance>step?currBalance-step:0);
                zkClient.writeData(this.serverPath,sd,stat.getVersion());
                return true;
            }catch (ZkBadVersionException e1){
                //ignore
            }catch (Exception e){
                e.printStackTrace();
                return  false;
            }
        }
    }
}
