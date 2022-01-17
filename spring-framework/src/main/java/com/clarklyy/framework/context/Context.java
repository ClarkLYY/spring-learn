package com.clarklyy.framework.context;

import com.clarklyy.framework.annotation.Autowire;
import com.clarklyy.framework.annotation.aspect.Aspect;
import com.clarklyy.framework.aop.aspect.ProxyChain;
import com.clarklyy.framework.aop.aspect.ProxyFactory;
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

public class Context {
    private Set<Class<?>> classSet = new HashSet<Class<?>>();
    private final Map<Class<?>, Object> context = new HashMap<Class<?>, Object>();
    //存放完整的实例（1级缓存）
    private final Map<String, Object> singletonObjects = new HashMap<String, Object>();
    //存放没有属性注入的实例（2级缓存）
    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>();
    //生成实例的工厂set（3级缓存）
    private final Map<String, ObjectFactory> singletonFactories = new HashMap<String, ObjectFactory>();

    //目标->切面类
    private final Map<Class<?>, List<Class<?>>> targetMap = new HashMap<>();

    public Context(){
        this.refresh();
    }

    /**
     * 容器初始化方法
     */
    public void refresh(){
        //扫描包名下的类，获取class
        this.classScan();
        //bean实例化
        this.beanInit();
        //bean属性注入
        this.populate();
        //扫描代理，生成代理类
        this.doProxy();
    }

    /**
     * 扫描类
     */
    private void classScan(){
        this.classSet = ClassScanUtil.getClassSet();
    }

    /**
     *bean实例化
     */
    private void beanInit(){
        for(Class<?> cls:classSet){
            if(cls.isAnnotationPresent(Controller.class)||cls.isAnnotationPresent(Bean.class)||cls.isAnnotationPresent(Service.class)){
                final Object bean = ReflectionUtil.newInstance(cls);
                context.put(cls, bean);
                earlySingletonObjects.put(cls.getSimpleName(), bean);
                this.invokeAware(bean);
            }
        }
    }

    /**
     * 属性输入
     */
    private void populate(){
        for(Map.Entry<Class<?>, Object> entry: context.entrySet()){
            Class<?> cls = entry.getKey();
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

    private void doProxy(){
        for(Class<?> cls:classSet){
            if(cls.isAnnotationPresent(Aspect.class)){
//                Aspect obj = (Aspect) ReflectionUtil.newInstance(cls);
                Class<?> targetCls = ReflectionUtil.getClassForName("com.clarklyy.framework.test.BeanA");
//                Method[] methods = targetCls.getMethods();
//                List<String> targetMethods = Arrays.asList(obj.methods());
//                List<Method> methodList = new ArrayList<>();
//                for(Method method:methods){
//                    if(targetMethods.contains(method.getName())){
//                        methodList.add(method);
//                    }
//                }
                if(targetMap.get(targetCls)!=null){
                    List<Class<?>> list = targetMap.get(targetCls);
                    list.add(cls);
                }else{
                    List<Class<?>> list = new ArrayList<>();
                    list.add(cls);
                    targetMap.put(targetCls,list);
                }
            }
        }
        for(Map.Entry<Class<?>, List<Class<?>>> entry:targetMap.entrySet()){
            Class<?> targetCls = entry.getKey();
            ProxyChain proxyChain = new ProxyChain(entry.getValue());
            Object proxyBean = ProxyFactory.getProxy(targetCls, proxyChain);
            singletonObjects.put(targetCls.getSimpleName(), proxyBean);
        }
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
