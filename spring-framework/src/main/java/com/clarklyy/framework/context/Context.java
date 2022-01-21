package com.clarklyy.framework.context;

import com.clarklyy.framework.annotation.Autowire;
import com.clarklyy.framework.annotation.aspect.Aspect;
import com.clarklyy.framework.aop.ProxyChain;
import com.clarklyy.framework.aop.ProxyFactory;
import com.clarklyy.framework.aware.BeanAware;
import com.clarklyy.framework.exception.BeanNotFoundException;
import com.clarklyy.framework.utils.ClassScanUtil;
import com.clarklyy.framework.annotation.Bean;
import com.clarklyy.framework.annotation.Controller;
import com.clarklyy.framework.annotation.Service;
import com.clarklyy.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class Context extends BeanRegister{
    private Set<Class<?>> classSet = new HashSet<>();
    //
    private final Map<String ,Class<?>> beanNameClassMap = new HashMap<>();
    //目标->切面类
    private final Map<Class<?>, List<Class<?>>> targetAspectMap = new HashMap<>();
    //目标->目标方法
    private final Map<Class<?>, List<Method>> targetMethodsMap = new HashMap<>();
    //存放完整的实例（1级缓存）
    private final Map<String, Object> singletonObjects = new HashMap<>();
    //存放没有属性注入的实例（2级缓存）
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();
    //生成实例的工厂set（3级缓存）
    private final Map<String, ObjectFactory> singletonFactories = new HashMap<>();

    public Context(){
        this.refresh();
    }

    /**
     * 容器初始化方法
     */
    public void refresh(){
        //扫描全局类
        this.scanClass();
        //扫描早期扩展中的切面信息
        this.scanProxy();
        //bean实例化
        this.beanInstance();
        //bean属性注入
        this.populate();

    }


    /**
     * 扫描类
     */
    private void scanClass(){
        this.classSet = ClassScanUtil.getClassSet();
    }

    /**
     *bean实例化
     */
    private void beanInstance(){
        for(Class<?> cls:classSet){
            if(cls.isAnnotationPresent(Controller.class)||cls.isAnnotationPresent(Bean.class)||cls.isAnnotationPresent(Service.class)){
                beanNameClassMap.put(cls.getSimpleName(), cls);
                //bean实例化
                final Object obj = ReflectionUtil.newInstance(cls);
                //bean放入三级缓存
                String beanName = cls.getSimpleName();
//                singletonFactories.put(beanName, new ObjectFactory() {
//                    @Override
//                    public Object getObject() {
//                        return getEarlyBeanReference(obj, cls);
//                    }
//                });
                earlySingletonObjects.put(cls.getSimpleName(), this.getEarlyBeanReference(obj, cls));
                this.invokeAware(obj);
            }
        }
    }

    /**
     * 对实例化的对象进行早期扩展，这里只用于生成代理对象
     * @param obj
     * @param cls
     * @return
     */
    private Object getEarlyBeanReference(Object obj, Class<?> cls) {
        //如果切面map中有这个类，说明需要进行代理
        if(targetAspectMap.containsKey(cls)){
            List<Class<?>> aspects = targetAspectMap.get(cls);
            Method[] methods = targetMethodsMap.get(cls).toArray(new Method[0]);
            ProxyChain proxyChain = new ProxyChain(aspects, methods, cls);
            return ProxyFactory.getProxy(proxyChain);
        }else{
            return obj;
        }
    }

    /**
     * 属性输入
     */
    private void populate(){
        for(Map.Entry<String, Object> entry: earlySingletonObjects.entrySet()){
            Class<?> cls = beanNameClassMap.get(entry.getKey());
            Object obj = entry.getValue();
            //获取当前类声明的属性（继承的不获取）
            Field[] fields = cls.getDeclaredFields();
            for(Field field:fields){
                if(field.isAnnotationPresent(Autowire.class)){
                    Class<?> beanFieldClass = field.getType();
                    Object fieldInstance = earlySingletonObjects.get(beanFieldClass.getSimpleName());
                    if(fieldInstance==null){
                        System.out.println("找不到:"+beanFieldClass.getSimpleName());
                    }
                    ReflectionUtil.setField(obj, field, fieldInstance);
                    System.out.println("往"+cls.getSimpleName()+"里注入"+beanFieldClass.getSimpleName()+"成功");
                }
            }
            singletonObjects.put(cls.getSimpleName(), obj);
        }
    }

    private void scanProxy(){
        for(Class<?> cls:classSet){
            if(cls.isAnnotationPresent(Aspect.class)){
                Aspect aspect = cls.getAnnotation(Aspect.class);
                Class<?> targetCls = ReflectionUtil.getClassForName(aspect.cls());
                Method[] methods = targetCls.getMethods();
                List<String> targetMethodList = Arrays.asList(aspect.methods());
                List<Method> methodList = new ArrayList<>();
                for(Method method:methods){
                    if(targetMethodList.contains(method.getName())){
                        methodList.add(method);
                    }
                }
                if(targetAspectMap.get(targetCls)!=null){
                    List<Class<?>> list = targetAspectMap.get(targetCls);
                    list.add(cls);
                }else{
                    List<Class<?>> list = new ArrayList<>();
                    list.add(cls);
                    targetAspectMap.put(targetCls,list);
                }

                if(targetMethodsMap.get(targetCls)!=null){
                    List<Method> list = targetMethodsMap.get(targetCls);
                    list.addAll(methodList);
                }else{
                    targetMethodsMap.put(targetCls, methodList);
                }
            }
        }
//        for(Map.Entry<Class<?>, List<Class<?>>> entry:targetAspectMap.entrySet()){
//            Class<?> targetCls = entry.getKey();
//            Method[] methods = targetMethodsMap.get(targetCls).toArray(new Method[0]);
//            ProxyChain proxyChain = new ProxyChain(entry.getValue(), methods, targetCls);
//            Object proxyBean = ProxyFactoryImpl.getProxy(proxyChain);
//            singletonObjects.put(targetCls.getSimpleName(), proxyBean);
//        }
    }

    /**
     * beanAware
     * @param bean
     */
    private void invokeAware(Object bean) {
        if(bean instanceof BeanAware){
            ((BeanAware) bean).setBeanAware();
        }
    }

    public Object getBean(String beanName){
        Object obj = singletonObjects.get(beanName);
        try{
            if(obj==null) throw new BeanNotFoundException();

        }catch (BeanNotFoundException e){
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(null);
        for(Integer i:list){
            System.out.println(i.toString());
        }
    }
}
