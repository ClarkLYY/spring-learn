package com.clarklyy.framework;

import com.clarklyy.framework.annotation.Autowire;
import com.clarklyy.framework.annotation.Bean;
import com.clarklyy.framework.aware.BeanAware;

@Bean
public class TestIoc implements BeanAware {
    @Autowire
    private BeanTest beanTest;

    public BeanTest getBeanTest() {
        return beanTest;
    }

    public void setBeanAware() {
        System.out.println("构造TestIoc");
    }
}
