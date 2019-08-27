package com.zhu.helper;

import com.zhu.annotation.Controller;
import com.zhu.annotation.Service;
import com.zhu.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * class 容器
 */
public class ClassHelper {
    private static final  Set<Class<?>> classSet ;

    static {
        classSet = ClassUtil.getClassSet(ConfigHelper.getAppBasePackage());
    }

    public static  Set<Class<?>> getClassSet(){
        return classSet;
    }

    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classs = new HashSet<>();
        for(Class<?> clazz: classSet){
            if(clazz.isAnnotationPresent(Service.class)){
                classs.add(clazz);
            }
        }
        return classs;
    }
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classs = new HashSet<>();
        for(Class<?> clazz: classSet){
            if(clazz.isAnnotationPresent(Controller.class)){
                classs.add(clazz);
            }
        }
        return classs;
    }
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classs = new HashSet<>();
        classs.addAll(getControllerClassSet());
        classs.addAll(getServiceClassSet());
        return classs;
    }

    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
        Set<Class<?>> classs = new HashSet<>();
        for(Class<?> clazz: classSet){
            if(superClass.isAssignableFrom(clazz)&&!superClass.equals(clazz)){
                classs.add(clazz);
            }
        }
        return classs;
    }

    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
        Set<Class<?>> classs = new HashSet<>();
        for(Class<?> clazz: classSet){
            if(clazz.isAnnotationPresent(annotationClass)){
                classs.add(clazz);
            }
        }
        return classs;
    }
}
