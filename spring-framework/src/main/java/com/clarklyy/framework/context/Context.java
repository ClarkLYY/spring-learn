package com.clarklyy.framework.context;

import com.clarklyy.framework.aware.BeanAware;
import com.clarklyy.framework.exception.BeanNotFoundException;
import com.clarklyy.framework.utils.ClassScanUtil;
import com.clarklyy.framework.annotation.Bean;
import com.clarklyy.framework.annotation.Controller;
import com.clarklyy.framework.annotation.Service;
import com.clarklyy.framework.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Context {
    private final Map<Class<?>, Object> context = new HashMap<Class<?>, Object>();


    public Context(){
        this.beanInit();

    }

    /**
     *初始化bean实例
     */
    public void beanInit(){
        Set<Class<?>> classSet = ClassScanUtil.getClassSet();
        for(Class<?> cls:classSet){
            if(cls.isAnnotationPresent(Controller.class)||cls.isAnnotationPresent(Bean.class)||cls.isAnnotationPresent(Service.class)){
                Object bean = ReflectionUtil.newInstance(cls);
                context.put(cls, bean);
                this.invokeAware(bean);
            }
        }
    }

    /**
     * beanAware
     * @param bean
     */
    private void invokeAware(Object bean) {
        if(bean instanceof BeanAware){
            ((BeanAware) bean).setBeanAware();
        }
    }

//    /**
//     *初始化bean实例
//     */
//    public void beanInit(){
//        Set<Class<?>> classSet = ClassScanUtil.getClassSet();
//        for(Class<?> cls:classSet){
//            if(cls.isAnnotationPresent(Controller.class)||cls.isAnnotationPresent(Bean.class)||cls.isAnnotationPresent(Service.class)){
//                Object bean = ReflectionUtil.newInstance(cls);
//                context.put(cls, bean);
//                this.invokeAware(bean);
//            }
//        }
//    }

    public Object getBean(String beanName){
        Class<?> target = null;
        for(Class<?> cls:context.keySet()){
            String className = cls.getSimpleName();
            if(className.equals(beanName)){
                target = cls;
                break;
            }
        }
        try{
            if(target==null) throw new BeanNotFoundException();

        }catch (BeanNotFoundException e){
            e.printStackTrace();
        }
        return context.get(target);
    }
}
