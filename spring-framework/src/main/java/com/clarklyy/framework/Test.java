package com.clarklyy.framework;

import com.clarklyy.framework.context.Context;

import java.util.Map;


public class Test {
    public static void main(String[] args) {
        //获取容器
        Context context = new Context();
        BeanTest beanTest = (BeanTest) context.getBean("BeanTest");
        TestIoc testIoc = (TestIoc) context.getBean("TestIoc");
        if(beanTest.getTestIoc()!=null){
            System.out.println("testIoc成功注入");
        }
        if(testIoc.getBeanTest()!=null){
            System.out.println("BeanTest成功注入");
        }
    }

}
