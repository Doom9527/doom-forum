package com.sky.utils;

public class BaseContext {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setToken(String id) {
        threadLocal.set(id);
    }

    public static String getToken() {
        return threadLocal.get();
    }
}
