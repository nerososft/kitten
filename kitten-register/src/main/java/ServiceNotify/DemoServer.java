package ServiceNotify;

import org.nero.kitten.common.core.Server;
import ServiceNotify.core.ServiceServer;
import org.nero.kitten.common.core.request.RegisterRequest;

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
