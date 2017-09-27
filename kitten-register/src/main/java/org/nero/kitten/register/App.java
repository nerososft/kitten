package org.nero.kitten.register;

import org.nero.kitten.register.core.RegisterServer;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/16
 * Time   下午2:39
 */
public class App {
    public static void main(String[] args){
        new ClassPathXmlApplicationContext("spring/spring-provider.xml", "spring/spring-consumer.xml");
    }
}
