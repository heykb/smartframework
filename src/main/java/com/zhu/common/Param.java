package com.zhu.common;

import com.zhu.util.CastUtil;

import java.util.Map;

/**
 * 封装参数Map，提供操作方法
 */
public class Param {
    private Map<String,Object> paramMap;

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Param(Map<String,Object> paramMap){
        this.paramMap=paramMap;
    }
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    public int getInt(String name){
        return CastUtil.castInt(paramMap.get(name));
    }
    public String getString(String name){
        return CastUtil.castString(paramMap.get(name));
    }
    public double getDouble(String name){
        return CastUtil.castDouble(paramMap.get(name));
    }
    public boolean getBoolean(String name){
        return CastUtil.castBoolean(paramMap.get(name));
    }


}
