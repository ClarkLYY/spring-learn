package com.clarklyy.framework;

import com.clarklyy.framework.context.Context;

import java.util.Map;


public class Test {
    public static void main(String[] args) {
        //获取容器
        Context context = new Context();
        BeanTest beanTest = (BeanTest) context.getBean("BeanTest");
        if(beanTest.testIoc==null){
            System.out.println(1);
        }
    }

}
