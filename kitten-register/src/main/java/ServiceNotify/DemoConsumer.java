package ServiceNotify;

import ServiceNotify.core.Client;
import ServiceNotify.core.ConsumerServer;
import main.java.core.Server;
import main.java.core.request.Request;
import main.java.core.request.ServiceRequestType;
import main.java.core.request.SubscribeRequest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   下午1:43
 */
public class DemoConsumer {
    public static void main(String[] args){

        new Thread(new Runnable() {
            public void run() {
                Server server = new ConsumerServer(8889);
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                Client<Request<SubscribeRequest>,SubscribeRequest> client
                        = new Client<Request<SubscribeRequest>, SubscribeRequest>(new InetSocketAddress("localhost",8888));
                try {
                    List<String> serviceTopic = new ArrayList<String>(10);
                    serviceTopic.add("com.service.IHello");
                    System.out.println(client.subscribe(new Request<SubscribeRequest>(
                            "request_01",
                            ServiceRequestType.SUBSCRIBE,
                            new SubscribeRequest(
                                    "localhost",
                                    8889,
                                    serviceTopic
                            ))).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        new Thread(new Runnable() {
//            public void run() {
//                Client<Request<InvokeRequest>,List<RegisterRequest>> client
//                        = new Client<Request<InvokeRequest>, List<RegisterRequest>>(new InetSocketAddress("localhost",8888));
//                try {
//                    System.out.println(client.findService(new Request<InvokeRequest>(
//                            "request_01",
//                            ServiceRequestType.FIND,
//                            new InvokeRequest(
//                                    "com.service.iHello"
//                            ))).toString());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }
}
