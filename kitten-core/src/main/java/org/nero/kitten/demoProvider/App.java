package org.nero.kitten.demoProvider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        new ClassPathXmlApplicationContext("spring/spring-provider.xml", "spring/spring-consumer.xml");
    }
}
