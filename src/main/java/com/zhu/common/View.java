package com.zhu.common;

import java.util.HashMap;
import java.util.Map;

public class View {

    //视图模型数据
    private Map<String,Object> model;

    //jsp视图路径
    private String path;

    public View(String path) {
        this.path = path;
        model = new HashMap<>();
    }
    public View addModel(String key, Object value){
        model.put(key,value);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public String getPath() {
        return path;
    }
}
