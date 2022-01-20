package com.clarklyy.framework.aop.aspect;

import com.clarklyy.framework.annotation.aspect.After;
import com.clarklyy.framework.annotation.aspect.Before;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;


public class ProxyFactory {
    public static <T> T getProxy(Class<T> cls, ProxyChain proxyChain){
        List<Class<?>> aspects = proxyChain.getAspects();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            for(Class<?> aspect:aspects){
                Method[] methods = aspect.getDeclaredMethods();
                for(Method aspectMethod : methods){
                    if(aspectMethod.isAnnotationPresent(Before.class)){
                        aspectMethod.invoke(aspect.newInstance());
                    }
                }
            }
            Object result = methodProxy.invokeSuper(o,proxyChain.getTargetMethod());
            for(Class<?> aspect:aspects){
                Method[] methods = aspect.getDeclaredMethods();
                for(Method aspectMethod : methods){
                    if(aspectMethod.isAnnotationPresent(After.class)){
                        aspectMethod.invoke(aspect.newInstance());
                    }
                }
            }
            return result;
        });
        return (T) enhancer.create();
    }
}
