package org.nero.kitten.demoProvider;

import org.nero.kitten.kitten.core.KittenService;

/**
 * Author :  root
 * Email  :  nerosoft@outlook.com
 * Date   :  16-11-17
 * Time   :  下午3:48
 */

@KittenService(IHelloService.class)

public class HelloService implements IHelloService {

    public String sayHello(String hello) {
        return "hello"+hello;
    }
}
