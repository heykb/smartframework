package com.zhu.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 */
public abstract class AspectProxy implements Proxy {
    private static final Logger log = LoggerFactory.getLogger(AspectProxy.class);
    @Override
    public final Object doProxy(ProxyChain proxyChain) throws Throwable{
        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] args = proxyChain.getArgs();

        Object re = null;
        begin();

       /**
        * Method targetMethod = proxyChain.getTargetMethod();
        *  if() 可在此对目标方法判断，实现方法级别的代理
        *  */
        try{
            if(intercept(targetClass,targetMethod,args)){
                before(targetClass,targetMethod,args);
                re = proxyChain.doProxyChain();
                after(targetClass,targetMethod,args);
            }else{
                re = proxyChain.doProxyChain();
            }
        }catch (Exception e){
            log.error("proxy failure",e);
            error(targetClass,targetMethod,args);
            throw e;
        }finally {
            end();
        }

        return re;
    }

    public void begin(){}

    public void before(Class<?> cls, Method method,Object[] orgs) throws Throwable{

    }
    public void after(Class<?> cls, Method method,Object[] orgs) throws Throwable{

    }

    public boolean intercept(Class<?> cls, Method method,Object[] orgs) throws Throwable{
        return true;
    }
    public void error(Class<?> cls, Method method,Object[] orgs) throws Throwable{

    }

    public void end(){}
}
