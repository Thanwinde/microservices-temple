package com.microservices.common.util;

public class UserContext {
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
