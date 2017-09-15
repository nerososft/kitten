package org.nero.kitten.registercenter.demo;

import com.sun.corba.se.spi.activation.Server;
import org.nero.kitten.registercenter.core.RegisterServer;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/15
 * Time   下午1:58
 */
public class DemoServer {
    public static void main(String[] args){
        RegisterServer server = new RegisterServer();
        server.start();
    }
}
