package com.zhu.util;

import org.apache.commons.lang3.StringUtils;

public class CastUtil {

    public static String castString(Object o){
        return castString(o,"");
    }
    public static String castString(Object o,String defaultValue){
        return o!=null? String.valueOf(o): defaultValue;
    }
    public static double castDouble(Object o){
        return castDouble(o,0);
    }
    //Object->String->double
    public static double castDouble(Object o, double defaultValue){
        double result = defaultValue;
        if(o!=null){
            String strValue = castString(o);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    result = Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    result = defaultValue;
                }
            }
        }
        return result;
    }
    public static long castLong(Object o){
        return castLong(o,0);
    }
    //Object->String->double
    public static long castLong(Object o, long defaultValue){
        long result = defaultValue;
        if(o!=null){
            String strValue = castString(o);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    result = Long.parseLong(strValue);
                }catch (NumberFormatException e){
                    result = defaultValue;
                }
            }
        }
        return result;
    }

    public static int castInt(Object o){
        return castInt(o,0);
    }
    //Object->String->int
    public static int castInt(Object o, int defaultValue){
        int result = defaultValue;
        if(o!=null){
            String strValue = castString(o);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    result = Integer.parseInt(strValue);
                }catch (NumberFormatException e){
                    result = defaultValue;
                }
            }
        }
        return result;
    }
    public static boolean castBoolean(Object o){
        return castBoolean(o,false);
    }
    public static boolean castBoolean(Object o,boolean defaultValue){
        boolean result = defaultValue;
        if(o!=null){
            result = Boolean.parseBoolean(castString(o));
        }
        return result;
    }
    public static void main(String[] args) {
        System.out.println(Boolean.parseBoolean("fdsfd"));
    }
}
