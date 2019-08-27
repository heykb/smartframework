package com.zhu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtil {
    private static final Logger log=  LoggerFactory.getLogger(ClassUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJson(T obj){
        String json;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("json convert error ",e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try {
            pojo = objectMapper.readValue(json,type);
        } catch (IOException e) {
            log.error("json parse error ",e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
