package com.clarklyy.framework.aop;

import com.clarklyy.framework.annotation.aspect.After;
import com.clarklyy.framework.annotation.aspect.Before;
import com.clarklyy.framework.aop.ProxyChain;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 代理对象生成类
 */
public class ProxyFactory {
    /**
     * 生成T的代理类
     * @param proxyChain
     * @param <T>
     * @return
     */
    public static <T> T getProxy(ProxyChain proxyChain){
        List<Class<?>> aspects = proxyChain.getAspects();
        Class<?> targetCls = proxyChain.getTargetCls();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetCls);
        enhancer.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            List<Method> targetMethodList = Arrays.asList(proxyChain.getTargetMethod());
            Object result;
            if(targetMethodList.contains(method)){
                for(Class<?> aspect:aspects){
                    Method[] methods = aspect.getDeclaredMethods();
                    for(Method aspectMethod : methods){
                        if(aspectMethod.isAnnotationPresent(Before.class)){
                            aspectMethod.invoke(aspect.newInstance());
                        }
                    }
                }
                result = methodProxy.invokeSuper(o,objects);
                Collections.reverse(aspects);
                for(Class<?> aspect:aspects){
                    Method[] methods = aspect.getDeclaredMethods();
                    for(Method aspectMethod : methods){
                        if(aspectMethod.isAnnotationPresent(After.class)){
                            aspectMethod.invoke(aspect.newInstance());
                        }
                    }
                }
            }else {
                result = methodProxy.invokeSuper(o,objects);
            }

            return result;
        });
        return (T) enhancer.create();
    }
}
