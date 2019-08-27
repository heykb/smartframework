package com.zhu.transactional;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zhu.aop.Proxy;
import com.zhu.aop.ProxyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy {
    private static final Logger log = LoggerFactory.getLogger(TransactionProxy.class);
    private static final ThreadLocal<Boolean> FLAG_HODLER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };
    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
       Object result;
       boolean flag = FLAG_HODLER.get();
        Method targetMethod = proxyChain.getTargetMethod();
        if(!flag && targetMethod.isAnnotationPresent(Transaction.class)){
            FLAG_HODLER.set(true);
            try{
                DatabaseHelper.beginTransaction();
                log.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                log.debug("commit transaction");
            }catch (Exception e){
                DatabaseHelper.rollbackTransaction();
                log.debug("rollback transaction");
                throw e;
            }finally {
               FLAG_HODLER.remove();
            }
        }else{
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}
