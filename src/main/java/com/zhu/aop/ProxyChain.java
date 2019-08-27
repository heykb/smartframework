package com.zhu.aop;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理链
 */
public class ProxyChain {


    private final Class<?> targetClass;
    private final Object targetObj;
    private final Object proxyObj;
    private final Method targetMethod;

    private final MethodProxy methodProxy;
    private final Object[] args;

    private List<Proxy> proxyList;


    private int proxyIndex = 0;
    public ProxyChain(Class<?> targetClass, Object targetObj, Object proxyObj, Method targetMethod, MethodProxy methodProxy, Object[] args, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObj = targetObj;
        this.proxyObj = proxyObj;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.args = args;
        this.proxyList = proxyList;
    }
    //执行代理连
    public Object doProxyChain() throws Throwable{

        Object re = null;
        //执行代理链中的横切逻辑
        if(proxyIndex<proxyList.size()){
            re = proxyList.get(proxyIndex++).doProxy(this);
        }else{
            //执行业务逻辑
            if(methodProxy==null){
                re = targetMethod.invoke(targetObj,args);
            }else{
                re= methodProxy.invokeSuper(targetObj,args);
            }
        }
        return re;
    }
    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTargetObj() {
        return targetObj;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getArgs() {
        return args;
    }
}
