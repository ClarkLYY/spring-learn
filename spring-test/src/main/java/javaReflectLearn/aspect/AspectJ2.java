package javaReflectLearn.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectJ2 {
    @Pointcut("execution(* javaReflectLearn.Test.init(..))")
    private void pointCut(){}

    @Before("pointCut()")
    void before(){
        System.out.println("before2");
    }

    @After("pointCut()")
    void after(){
        System.out.println("after2");
    }

    @Around("pointCut()")
    void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before2");
        joinPoint.proceed();
        System.out.println("around after2");
    }

    @AfterReturning("pointCut()")
    void afterRunning(){
        System.out.println("AfterReturning2");
    }
}
