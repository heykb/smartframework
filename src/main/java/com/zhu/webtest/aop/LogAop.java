package com.zhu.webtest.aop;

import com.zhu.annotation.Controller;
import com.zhu.aop.AspectProxy;
import com.zhu.aop.annotation.Aspect;
import com.zhu.webtest.Controller.TestController;

import java.lang.reflect.Method;

@Aspect(Controller.class)
public class LogAop extends AspectProxy {
    @Override
    public void before(Class<?> cls, Method method, Object[] orgs) throws Throwable {
        System.out.println(String.format("%s类%s类 %s方法 执行",cls.getName(), TestController.class.getName(),method.getName()));
    }
}
