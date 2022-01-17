package com.clarklyy.framework.aop.aspect;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyChain {
    private  List<Class<?>> aspects;
    private Object targetObject;
    private Method targetMethod;

    public void setAspects(List<Class<?>> aspects) {
        this.aspects = aspects;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public ProxyChain(List<Class<?>> aspects){
        this.aspects = aspects;
    }

    public  List<Class<?>> getAspects() {
        return aspects;
    }
}
