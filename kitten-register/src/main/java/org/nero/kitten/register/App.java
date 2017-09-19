package org.nero.kitten.register;

import org.nero.kitten.register.core.RegisterServer;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/16
 * Time   下午2:39
 */
public class App {
    public static void main(String[] args){
        RegisterServer server = new RegisterServer();
        server.start();
    }
}
