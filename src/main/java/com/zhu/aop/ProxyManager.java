package com.zhu.aop;

import com.zhu.aop.test.GreetingImpl;
import com.zhu.helper.BeanHelper;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyManager {

    public static <T> T createProxy(final Class<T> targetClass, final List<Proxy> proxyList){

        Class<?>[] interfaces = targetClass.getInterfaces();
        Object o=null;
        if(ArrayUtils.isNotEmpty(interfaces)){
            o = java.lang.reflect.Proxy.newProxyInstance(targetClass.getClassLoader(),interfaces,new JdkDynamicProxy(proxyList, BeanHelper.getBean(targetClass)));
        }else{
            o = Enhancer.create(targetClass, new MethodInterceptor() {
                @Override
                public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                    ProxyChain proxyChain = new ProxyChain(targetClass,target,null,method,methodProxy,args,proxyList);
                    return proxyChain.doProxyChain();
                }
            });
        }



       //Object o = new CglibDynamicProxy(proxyList).getProxy(targetClass);
        return (T) o;
    }
}
