package com.zhu.helper;

import com.zhu.common.ConfigConstant;
import com.zhu.util.PropsUtil;

import java.util.Properties;

/**
 * 加载配置文件属性类
 */
public class ConfigHelper {
    public static final Properties  CONFIG_PROPERTIES = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE.value());

    /**
     * 获取jdbc驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.JDBC_DRIVER.value());
    }

    /**
     * 获取jdbc url
     * @return
     */
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.JDBC_UTL.value());
    }
    /**
     * 获取jdbc username
     * @return
     */
    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.JDBC_UERNAME.value());
    }
    /**
     * 获取jdbc password
     * @return
     */
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.JDBC_PASSWORD.value());
    }
    /**
     * 获取应用基础包名
     * @return
     */
    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.APP_BASE_PACKAGE.value());
    }
    /**
     * 获取应用jsp路径
     * @return
     */
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.APP_JSP_PATH.value(),"/WEB-INF/view/");
    }
    /**
     * 获取静态资源路径
     * @return
     */
    public static String getJdbcAppAssetPath(){
        return PropsUtil.getString(CONFIG_PROPERTIES,ConfigConstant.APP_ASSET_PATH.value(), "/src/webapp/asset/");
    }
}
