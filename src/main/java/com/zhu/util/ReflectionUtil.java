package com.zhu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    private static final Logger log=  LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 实例化bean
     * @param cls
     * @return
     */
    public static  Object newInstance(Class<?> cls){
        Object instance;
        try {
            instance = cls.newInstance();
        } catch (Exception e) {
            log.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    /**
     * 调用方法
     * @param obj
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object obj, Method method,Object...args){
        Object result;

        try {
            //取消访问检查，可提高速度
            method.setAccessible(true);
            result =  method.invoke(obj,args);
        } catch (Exception e) {
            log.error("invoke method failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量的值
     * @param obj
     * @param field
     * @param value
     */
    public static void setFiled(Object obj, Field field, Object value){
        try {
            //取消访问检查，可提高速度
            field.setAccessible(true);
            field.set(obj,value);
        } catch (Exception e) {
            log.error("set field failue",e);
            throw new RuntimeException(e);
        }
    }
}
