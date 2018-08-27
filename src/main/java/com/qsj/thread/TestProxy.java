package com.qsj.thread;

import java.lang.reflect.Proxy;

public class TestProxy {
    public static void main(String[] args) {
        ProxyTestHandle testHandle = new ProxyTestHandle(MyInterfaceImpl.class);
        MyInterface myInterface = (MyInterface)Proxy.newProxyInstance(TestProxy.class.getClassLoader(),new Class[]{MyInterface.class},testHandle);
        myInterface.execute();
    }
}
