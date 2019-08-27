package com.zhu.common;

public enum RequestMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    public static RequestMethod get(String method){
        RequestMethod result = null;
        for(RequestMethod requestMethod:RequestMethod.values()){
            if(method.equalsIgnoreCase(requestMethod.name())){
                result=requestMethod;
                break;
            }
        }
        if(result==null){
            throw new IllegalArgumentException("there is no method:"+method);
        }
        return result;
    }
}
