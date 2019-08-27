package com.zhu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流操作工具类
 */
public final class StreamUtil {
    private static final Logger log = LoggerFactory.getLogger(StreamUtil.class);
    public static  String getString(InputStream is){

        StringBuilder stringBuilder = new StringBuilder();
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) !=null){
                stringBuilder.append(line);
            }
        }catch (Exception e){
            log.error("get string failure",e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
