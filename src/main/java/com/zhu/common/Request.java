package com.zhu.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class Request {
    private RequestMethod method;
    private String path;

    public Request(RequestMethod method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method.name();
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
       return EqualsBuilder.reflectionEquals(this,o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public static void main(String[] args) {
        System.out.println(RequestMethod.DELETE.name());
    }
}
