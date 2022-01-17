package javaReflectLearn;

import javaReflectLearn.aspect.Aspect;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Test implements BeanNameAware {

    public void init(){
        System.out.println("init");
    }


    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Test test = Aspect.getProxy(Test.class, new String[]{"javaReflectLearn.aspect.AspectImplA"});
        test.init();
//        ApplicationContext context = new ClassPathXmlApplicationContext("beanTest.xml");
//        Test proxy = (Test) context.getBean("test");
//        proxy.init();
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("生成Test");
    }
}
