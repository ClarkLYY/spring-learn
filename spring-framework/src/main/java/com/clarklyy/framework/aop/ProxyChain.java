package com.clarklyy.framework.aop;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理链，封装切面list和目标类信息
 */
public class ProxyChain {
    private final List<Class<?>> aspects;
    private final Class<?> targetCls;
    private final Method[] targetMethod;

    public ProxyChain(List<Class<?>> aspects, Method[] targetMethod, Class<?> targetCls){
        this.aspects = aspects;
        this.targetMethod = targetMethod;
        this.targetCls = targetCls;
    }

    public Class<?> getTargetCls() {
        return targetCls;
    }

    public Method[] getTargetMethod() {
        return targetMethod;
    }



    public  List<Class<?>> getAspects() {
        return aspects;
    }
}
