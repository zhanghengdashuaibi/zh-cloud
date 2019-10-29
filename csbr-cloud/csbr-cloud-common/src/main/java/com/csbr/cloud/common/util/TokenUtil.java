package com.csbr.cloud.common.util;

/**
 * token工具类
 * @author arthas on 2019/10/15
 */
public class TokenUtil {
    /**
     * 使用ThreadLocal类来保存token信息
     */
    private static ThreadLocal<String> local = new ThreadLocal<>();

    public static void setToken(String token){
        local.set(token);
    }

    public static String getToken(){
        return local.get();
    }
}
