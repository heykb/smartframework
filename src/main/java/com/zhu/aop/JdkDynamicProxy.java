package com.zhu.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class JdkDynamicProxy implements InvocationHandler {
    private List<Proxy> proxyList;

    private Object target;

    public JdkDynamicProxy(List<Proxy> proxyList,Object target) {
        this.target=target;
        this.proxyList = proxyList;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ProxyChain proxyChain = new ProxyChain(target.getClass(),target,proxy,method,null,args,proxyList);
        return proxyChain.doProxyChain();
    }
}
