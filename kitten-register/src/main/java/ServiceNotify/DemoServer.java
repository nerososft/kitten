package ServiceNotify;

import ServiceNotify.core.Server;
import ServiceNotify.core.ServiceServer;
import ServiceNotify.request.RegisterRequest;

import java.io.IOException;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   下午1:24
 */
public class DemoServer {
    public static void main(String[] aa){
        Server server = new ServiceServer<RegisterRequest>(8888);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
