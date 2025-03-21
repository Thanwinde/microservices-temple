package com.microservices.common.util;

public class UserContext {
    //采用ThreadLocal来传递用户信息
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public  static String getUserId() {
        return threadLocal.get();
    }

    public static void setUserId(String userId) {
        threadLocal.set(userId);
    }

    public static void removeUserId() {
        threadLocal.remove();
    }

}
