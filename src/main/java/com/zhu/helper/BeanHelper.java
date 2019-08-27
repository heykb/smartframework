package com.zhu.helper;

import com.zhu.annotation.Inject;
import com.zhu.util.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * bean 容器:可以获取bean
 */
public class BeanHelper {
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<>();
    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for(Class<?> cls: beanClassSet){
            Object obj = ReflectionUtil.newInstance(cls);
            BEAN_MAP.put(cls,obj);
        }
        for(Map.Entry<Class<?>,Object> entry:BEAN_MAP.entrySet()){
            Object obj = entry.getValue();
            Class<?> cls = entry.getKey();
            Field[] fields = cls.getDeclaredFields();
            if(ArrayUtils.isNotEmpty(fields)){
                for(Field field:fields){
                    if(field.isAnnotationPresent(Inject.class)){
                        ReflectionUtil.setFiled(obj,field,getBean(field.getType()));
                    }
                }
            }
        }
    }

    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by clss :"+cls.getName());
        }
        return (T)BEAN_MAP.get(cls);
    }
    public static void setBean(Class<?> cls,Object obj){
        BEAN_MAP.put(cls,obj);
    }
}
