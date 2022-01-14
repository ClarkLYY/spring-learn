package javaReflectLearn.aspect;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface Aspect {
    static <T> T getProxy(Class<T> cls, String[] aspects) throws IllegalAccessException, InstantiationException {
        List<Aspect> aspectList = Arrays.stream(aspects).map(aspectName -> {
            try {
                return (Aspect)Class.forName(aspectName).getConstructor().newInstance();
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        T instance = cls.newInstance();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(cls);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                for(Aspect aspect:aspectList){
                    aspect.before();
                }
                Object result = methodProxy.invokeSuper(o,objects);
                for(Aspect aspect:aspectList){
                    aspect.after();
                }
                return result;
            }
        });
        return (T) enhancer.create();
    }

    void before();
    void after();

}
