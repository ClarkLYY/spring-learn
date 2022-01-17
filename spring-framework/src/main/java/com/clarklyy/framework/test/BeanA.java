package com.clarklyy.framework.test;

import com.clarklyy.framework.annotation.Autowire;
import com.clarklyy.framework.annotation.Controller;
import com.clarklyy.framework.aware.BeanAware;

@Controller
public class BeanA implements BeanAware {
    @Autowire
    private BeanB beanB;

    public void init(){
        System.out.println("init beanA");
    }

    public BeanB getBeanB() {
        return beanB;
    }

    public void setBeanAware() {
        System.out.println("构造BeanA");
    }
}
