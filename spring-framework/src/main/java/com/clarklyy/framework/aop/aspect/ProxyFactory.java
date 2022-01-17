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
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                for(Class<?> aspect:aspects){
                    Method[] methods = aspect.getDeclaredMethods();
                    for(Method aspectMethod : methods){
                        if(aspectMethod.isAnnotationPresent(Before.class)){
                            aspectMethod.invoke(aspect.newInstance());
//                            System.out.println(1);
                        }
                    }
                }
                Object result = methodProxy.invokeSuper(o,objects);
                for(Class<?> aspect:aspects){
                    Method[] methods = aspect.getDeclaredMethods();
                    for(Method aspectMethod : methods){
                        if(aspectMethod.isAnnotationPresent(After.class)){
                            aspectMethod.invoke(aspect.newInstance());
                        }
                    }
                }
                return result;
            }
        });
        return (T) enhancer.create();
    }
}
