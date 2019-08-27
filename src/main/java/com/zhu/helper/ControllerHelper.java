package com.zhu.helper;

import com.zhu.annotation.Action;
import com.zhu.common.Handler;
import com.zhu.common.Request;
import com.zhu.common.RequestMethod;
import com.zhu.util.StreamUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 封装request和handler的映射，通过请求方法和路径获取action处理方法
 */
public class ControllerHelper {

    private static final  Map<Request, Handler> ACTION_MAP;


    static {
        ACTION_MAP = new HashMap<>();
        Set<Class<?>> classSet = ClassHelper.getControllerClassSet();
        if(CollectionUtils.isNotEmpty(classSet)){
            for(Class<?> cls: classSet){
                Method[] methods = cls.getDeclaredMethods();
                if(ArrayUtils.isNotEmpty(methods)){
                    for(Method method: methods){
                        if(method.isAnnotationPresent(Action.class)){
                            String path = method.getAnnotation(Action.class).value();
                            RequestMethod requestMethods = method.getAnnotation(Action.class).method();
                            if(StringUtils.isNotEmpty(path)){
                                Request request = new Request(requestMethods,path);
                                Handler handler = new Handler(cls,method);
                                ACTION_MAP.put(request,handler);
                            }
                           /* if(mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if(ArrayUtils.isNotEmpty(array)&&array.length==2){
                                    String requestMethod = array[0];
                                    String requestUrl = array[1];
                                    Request request = new Request(requestMethod,requestUrl);
                                    Handler handler = new Handler(cls,method);
                                    ACTION_MAP.put(request,handler);
                                }
                            }*/
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(RequestMethod requestMethod,String requestPath){
        Request request = new Request(requestMethod,requestPath);
        if(!ACTION_MAP.containsKey(request)){
            throw new RuntimeException("can not found aciton for"+requestMethod+":"+requestPath);
        }
        return ACTION_MAP.get(request);
    }
}
