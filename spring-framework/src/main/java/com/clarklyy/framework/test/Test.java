package com.clarklyy.framework.test;

import com.clarklyy.framework.context.Context;


public class Test {
    public static void main(String[] args) {
        //获取容器
        Context context = new Context();
        BeanA beanA = (BeanA) context.getBean("BeanA");
        BeanB beanB = (BeanB) context.getBean("BeanB");
        if(beanB.getBeanA()!=null){
            System.out.println("属性注入成功");
        }
        beanA.init();
    }
}
