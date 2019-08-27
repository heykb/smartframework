package com.zhu.common;

public enum  ConfigConstant {

    CONFIG_FILE("smart.properties"),
    JDBC_DRIVER("smart.framework.jdbc.driver"),
    JDBC_UTL("smart.framework.jdbc.url"),
    JDBC_PASSWORD("smart.framework.jdbc.password"),
    JDBC_UERNAME("smart.framework.jdbc.username"),
    APP_BASE_PACKAGE("smart.framework.app.base_package"),
    APP_JSP_PATH("smart.framework.app.jsp_path"),
    APP_ASSET_PATH("smart.framework.app.asset_path");

    private final String value;

    ConfigConstant(String value) {
        this.value = value;
    }
    public String value(){
        return value;
    }
}
