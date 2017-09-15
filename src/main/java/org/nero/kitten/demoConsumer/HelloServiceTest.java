package org.nero.kitten.demoConsumer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nero.kitten.demoProvider.service.IHelloService;
import org.nero.kitten.kitten.core.KittenProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Author :  root
 * Email  :  nerosoft@outlook.com
 * Date   :  16-11-17
 * Time   :  下午4:48
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-consumer.xml")
public class HelloServiceTest {

    @Autowired
    private KittenProxy rpcProxy;

    @Test
    public void helloTest() {
        IHelloService helloService = rpcProxy.create(IHelloService.class);
        String result = helloService.sayHello("World");
        Assert.assertEquals("Hello! World", result);
    }

}
