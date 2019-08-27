package com.zhu.aop;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zhu.annotation.Service;
import com.zhu.aop.annotation.Aspect;
import com.zhu.helper.BeanHelper;
import com.zhu.helper.ClassHelper;
import com.zhu.transactional.TransactionProxy;
import org.apache.commons.collections4.SetUtils;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * aop加载类，加载目标类到代理对象链的映射。
 */
public class AopHelper {

    static {
        try {
            Map<Class<?>,Set<Class<?>>> proxyMap = createProxyMap();
            //一个目标类对应一个代理链
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for(Map.Entry<Class<?>, List<Proxy>> entry:targetMap.entrySet()){
                Class<?> targetClass = entry.getKey();
                List<Proxy> proxyList = entry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass,proxyList);
                //用代理对象覆盖原对象
                BeanHelper.setBean(targetClass,proxy);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取@Aspect 代理目标类
     * @param aspect
     * @return
     * @throws Throwable
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClasss = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if(annotation!=null && !annotation.equals(Aspect.class)){

            //获取某注解的所有类
            targetClasss.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClasss;
    }

    /**
     * 获取@Aspect代理类与目标类的映射,一个代理类对应多个目标类,
     * 添加事务代理，@Service类是目标类，代理类是TransactionProxy
     * @return
     * @throws Exception
     */
    public static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<>();
        //获取所有AspectProxy的子类
        Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
        if(proxyClassSet!=null){
            //遍历所有AspectProxy的子类
            for (Class<?> proxyClass:proxyClassSet){
                //只有注解了Aspect.class
                if(proxyClass.isAnnotationPresent(Aspect.class)){
                    Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                    //获取该Aspect注解所映射的所有目标类
                    Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                    //将代理类映射目标类
                    proxyMap.put(proxyClass,targetClassSet);
                }
            }
        }
        //给@Service类添加代理类
        proxyMap.put(TransactionProxy.class,ClassHelper.getClassSetByAnnotation(Service.class));
        return proxyMap;
    }

    /**
     * 一个目标类对应一个代理对象链
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();

        //遍历（一个代理类对应多个目标类）的map
        for(Map.Entry<Class<?>,Set<Class<?>>> entry: proxyMap.entrySet()){
            Class<?> proxyClass = entry.getKey();
            Set<Class<?>> targetClassSet = entry.getValue();

            for(Class<?> targetClass: targetClassSet){
                Proxy proxy = (Proxy) proxyClass.newInstance();
                //已经存在，则追加该代理类到代理链中
                if(targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else{
                    //否则，新建代理链
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }
}
