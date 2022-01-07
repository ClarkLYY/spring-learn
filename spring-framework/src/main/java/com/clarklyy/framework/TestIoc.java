package com.clarklyy.framework;

import com.clarklyy.framework.annotation.Bean;
import com.clarklyy.framework.aware.BeanAware;

@Bean
public class TestIoc implements BeanAware {
    public void setBeanAware() {
        System.out.println("构造TestIoc");
    }
}
