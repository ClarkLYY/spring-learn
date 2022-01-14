package javaReflectLearn;

import javaReflectLearn.aspect.Aspect;

public class Test {

    public void init(){
        System.out.println("init");
    }


    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Test test = Aspect.getProxy(Test.class, new String[]{"javaReflectLearn.aspect.AspectImplA"});
        test.init();
    }
}
