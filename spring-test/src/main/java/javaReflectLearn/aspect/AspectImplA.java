package javaReflectLearn.aspect;

public class AspectImplA implements Aspect {
    @Override
    public void before() {
        System.out.println("before");
    }

    @Override
    public void after() {
        System.out.println("after");
    }
}
