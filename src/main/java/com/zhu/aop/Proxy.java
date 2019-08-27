package com.zhu.aop;

/**
 * 代理接口
 */
public interface Proxy {

    Object  doProxy(ProxyChain proxyChain) throws Throwable;
}
