package com.zhu.aop.test;

import com.zhu.aop.Proxy;
import com.zhu.aop.ProxyManager;
import com.zhu.webtest.aop.LogAop;

import java.util.ArrayList;

public class Client {
    public static void main(String[] args) {
        LogAop aop = new LogAop();
        ArrayList<Proxy> proxies = new ArrayList<>();
        proxies.add(aop);
        Greeting greeting = ProxyManager.createProxy(GreetingImpl.class,proxies);
        greeting.sayHello("fdfsdf");
    }
}
