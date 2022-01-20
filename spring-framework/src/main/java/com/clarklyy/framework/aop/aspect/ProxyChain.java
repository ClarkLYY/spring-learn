package com.clarklyy.framework.aop.aspect;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyChain {
    private  List<Class<?>> aspects;
    private Method[] targetMethod;

    public void setAspects(List<Class<?>> aspects) {
        this.aspects = aspects;
    }

    public Method[] getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method[] targetMethod) {
        this.targetMethod = targetMethod;
    }

    public ProxyChain(List<Class<?>> aspects, Method[] targetMethod){
        this.aspects = aspects;
        this.targetMethod = targetMethod;
    }

    public  List<Class<?>> getAspects() {
        return aspects;
    }
}
