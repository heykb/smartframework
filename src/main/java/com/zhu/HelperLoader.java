package com.zhu;

import com.zhu.annotation.Controller;
import com.zhu.aop.AopHelper;
import com.zhu.helper.BeanHelper;
import com.zhu.helper.ClassHelper;
import com.zhu.helper.ControllerHelper;
import com.zhu.helper.IocHelper;
import com.zhu.util.ClassUtil;
import org.apache.log4j.BasicConfigurator;

/**
 * 加载Helper类
 */
public final class HelperLoader {
    public static void init(){
        Class<?>[] helperClass = new Class[]{
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for(Class<?> cls:helperClass){

            ClassUtil.loadClass(cls.getName(),true);
        }
        BasicConfigurator.configure();
    }
}
