package com.clarklyy.framework.utils;

import java.lang.reflect.Field;

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

    /**
     * 设置属性注入值
     */
    public static void setField(Object obj, Field field, Object value){
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
