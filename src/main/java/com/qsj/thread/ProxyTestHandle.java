package com.qsj.thread;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyTestHandle implements InvocationHandler {

    private  Object target;

    public ProxyTestHandle(Class clazz){
        try {
            this.target = clazz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        before();
        Object res = method.invoke(target,args);
        after();
        return res;
    }

    private  void before(){
        System.out.println("before execute");
    }

    private  void after(){
        System.out.println("after execute");
    }
}
