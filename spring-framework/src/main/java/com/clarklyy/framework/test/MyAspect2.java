package com.clarklyy.framework.test;

import com.clarklyy.framework.annotation.aspect.After;
import com.clarklyy.framework.annotation.aspect.Aspect;
import com.clarklyy.framework.annotation.aspect.Before;

@Aspect(cls = "com.clarklyy.framework.test.BeanB", methods = {"init"})
public class MyAspect2 {


    @Before
    public void before(){
        System.out.println("这是AspectB的before");
    }
//
//    @Before
//    public void before2(){
//        System.out.println("这是AspectB的before2");
//    }

    @After
    public void after(){
        System.out.println("这是AspectB的after");
    }
//    @After
//    public void after2(){
//        System.out.println("这是AspectB的after2");
//    }
}
