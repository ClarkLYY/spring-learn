package com.clarklyy.framework.test;

import com.clarklyy.framework.annotation.aspect.After;
import com.clarklyy.framework.annotation.aspect.Aspect;
import com.clarklyy.framework.annotation.aspect.Before;

@Aspect(cls = "com.clarklyy.framework.test.BeanA", methods = {"init"})
public class MyAspect2 {


    @Before
    public void before(){
        System.out.println("这是before2");
    }
    @After
    public void after(){
        System.out.println("这是after2");
    }
}
