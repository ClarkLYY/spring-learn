package com.clarklyy.framework.utils;

public class ReflectionUtil {
    /**
     * 创建实例
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance = null;
        try {
            instance= cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("new instance failure");
        }
        return instance;
    }
}
