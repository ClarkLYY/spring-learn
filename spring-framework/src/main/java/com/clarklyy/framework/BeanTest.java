package com.clarklyy.framework;

import com.clarklyy.framework.annotation.Autowire;
import com.clarklyy.framework.annotation.Bean;
import com.clarklyy.framework.annotation.Controller;
import com.clarklyy.framework.aware.BeanAware;

@Controller
public class BeanTest implements BeanAware {
    @Autowire
    TestIoc testIoc;

    public void setBeanAware() {
        System.out.println("构造BeanTest");
    }
}
