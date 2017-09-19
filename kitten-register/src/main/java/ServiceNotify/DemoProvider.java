package ServiceNotify;

import ServiceNotify.core.Client;
import ServiceNotify.request.RegisterRequest;
import ServiceNotify.request.Request;
import ServiceNotify.request.ServiceRequestType;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   下午1:42
 */
public class DemoProvider {

    public static void main(String[] args){
        Client<Request<RegisterRequest>,RegisterRequest> client
                = new Client<Request<RegisterRequest>, RegisterRequest>(new InetSocketAddress("localhost",8888));
        try {
            System.out.println(client.registerService(new Request<RegisterRequest>(
                    "request_01",
                    ServiceRequestType.REGISTER,
                    new RegisterRequest("192.168.7.3",
                            1234,
                            "com.service.IHello",
                            null,
                            null))).toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
