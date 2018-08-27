package com.qsj.netty.balance.client;

public interface BalanceProvider<T> {
    public T getBalanceItem();
}
