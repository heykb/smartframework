package com.zhu.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class CglibDynamicProxy implements MethodInterceptor {


    private List<Proxy> proxyList;

    public CglibDynamicProxy(List<Proxy> proxyList){
        this.proxyList=proxyList;
    }

    //向下转型，忽略警告
    @SuppressWarnings("unchecked")
    public  <T> T getProxy(Class<T> cls){
        return (T)Enhancer.create(cls,this);
    }

    @Override
    public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        //ProxyChain proxyChain = new ProxyChain(target.getClass(),target,method,methodProxy,args,proxyList);
        ProxyChain proxyChain = new ProxyChain(target.getClass(),target,null,method,methodProxy,args,proxyList);
        Object re = proxyChain.doProxyChain();
        return re;
    }
}
