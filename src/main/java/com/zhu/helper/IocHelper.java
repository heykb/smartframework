package com.zhu.helper;

import com.zhu.annotation.Inject;
import com.zhu.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入实现类
 */
public class IocHelper {
    static {
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        for(Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()){
            Class<?> cls = beanEntry.getKey();
            Object objInstance = beanEntry.getValue();
            Field[] fields = cls.getDeclaredFields();
            for(Field field: fields){
                if(field.isAnnotationPresent(Inject.class)){
                    Object beanFieldInstance = beanMap.get(field.getType());
                    if(beanFieldInstance!=null){
                        ReflectionUtil.setFiled(objInstance,field,beanFieldInstance);
                    }
                }
            }
        }
    }
}
