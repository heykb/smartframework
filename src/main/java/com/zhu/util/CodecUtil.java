package com.zhu.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.security.provider.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CodecUtil {

    private static final Logger log = LoggerFactory.getLogger(CodecUtil.class);

    public static String encodeURL(String source){
        String result;
        try {
            result = URLEncoder.encode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encode url failure",e);
        }
        return result;
    }
    public static String decodeURL(String source){
        String result;
        try {
            result = URLDecoder.decode(source,"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("decode url failure",e);
        }
        return result;
    }
    public static String md5(String source){
        return DigestUtils.md5Hex(source);
    }

    public static void main(String[] args) {
        String s = decodeURL("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&srcqid=3406299309720699674&tn=57028281_hao_pg&wd=%E5%97%9C%E8%A1%80%E6%B3%95%E5%8C%BB1%E6%97%A0%E8%80%BB%E4%B9%8B%E5%BE%92&oq=%25E5%2597%259C%25E8%25A1%2580%25E6%25B3%2595%25E5%258C%25BB1&rsv_pq=958fb3e200326748&rsv_t=228f%2BVHOBOVbiD%2Bk0wnqaMAylvR8vu3oqGOd9kuCdj1T53TPGyFGYeVt3v3fmWqzD1eHuE52&rqlang=cn&rsv_enter=0&rsv_dl=tb&inputT=241&rsv_sug3=140&bs=%E5%97%9C%E8%A1%80%E6%B3%95%E5%8C%BB1");
//        s = decodeURL(s);
        System.out.println(s);
    }
}
