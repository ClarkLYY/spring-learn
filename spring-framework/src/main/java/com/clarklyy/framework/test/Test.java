package com.clarklyy.framework.test;

import com.clarklyy.framework.context.Context;


public class Test {
    public static void main(String[] args) throws InterruptedException {
        //获取容器
        Context context = new Context();
        BeanA beanA = (BeanA) context.getBean("BeanA");
        BeanB beanB = (BeanB) context.getBean("BeanB");
        if(beanB.getBeanA()!=null){
            System.out.println("属性注入成功");
        }
        System.out.println("===========================");
        beanA.init(1);
        System.out.println("===========================");
        beanA.noProxy(1,1);
        System.out.println("===========================");
        BeanB beanB2 = beanA.getBeanB();

        beanB.init();
        System.out.println("===========================");
        beanB2.init();
//        beanB.init();
    }
}
