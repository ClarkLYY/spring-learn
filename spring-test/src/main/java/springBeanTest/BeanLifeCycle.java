package springBeanTest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import springBeanTest.config.Appconfig;

public class BeanLifeCycle {

    public static void main(String[] args) throws CloneNotSupportedException {

        Person p = new Person();
        Person p2 = (Person) p.clone();
        System.out.println("现在开始初始化容器");

        ApplicationContext factory = new AnnotationConfigApplicationContext(Appconfig.class);
        System.out.println("容器初始化成功");
        //得到Preson，并使用
        Person person = factory.getBean("person",Person.class);
        System.out.println(person);

        System.out.println("现在开始关闭容器！");
        ((ClassPathXmlApplicationContext)factory).registerShutdownHook();
    }
}
