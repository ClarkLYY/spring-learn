package com.clarklyy.framework.test;

import com.clarklyy.framework.annotation.Autowire;
import com.clarklyy.framework.annotation.Bean;
import com.clarklyy.framework.aware.BeanAware;

@Bean
public class BeanB implements BeanAware {
    @Autowire
    private BeanA beanA;

    public BeanA getBeanA() {
        return beanA;
    }

    public void init(){
        System.out.println("init beanB");
    }

    public void setBeanAware() {
        System.out.println("构造BeanB");
    }
}
