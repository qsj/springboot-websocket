package com.qsj.netty.balance.server;

public interface BalanceUpdateProvider {
    public boolean addBalance(int step);
    public boolean reduceBalance(int step);
}
