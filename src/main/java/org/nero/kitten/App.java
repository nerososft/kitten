package org.nero.kitten;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        new ClassPathXmlApplicationContext("spring-provider.xml");
    }
}
