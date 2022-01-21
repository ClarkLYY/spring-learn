package com.clarklyy.framework.context;

public interface BeanPostProcessor {
    void postProcessBeforeInitialization();
    void postProcessAfterInitialization();
}
