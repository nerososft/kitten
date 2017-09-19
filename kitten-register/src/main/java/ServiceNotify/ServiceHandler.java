package ServiceNotify;

import org.nero.kitten.register.core.dto.ServiceRequest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:19
 */
public class ServiceHandler<T> {

    private Socket socket;
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ServiceHandler( Socket socket) {
        this.socket = socket;
    }

    public void handle(Request<T> request) throws IOException {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream  = new ObjectOutputStream(socket.getOutputStream());
            switch (request.getServiceRequestType()) {
                case SUBSCRIBE: //消费者订阅

                    SubscribeRequest subscribeRequest  = (SubscribeRequest) request.getData();
                    System.out.println("服务订阅："+subscribeRequest);

                    for(String serviceName:subscribeRequest.getServiceList()){
                        System.out.println(serviceName);
                        ServiceServer.addNotify(serviceName,new InetSocketAddress(subscribeRequest.getIp(),subscribeRequest.getPort())); //将客户端添加至订阅列表
                    }

                    objectOutputStream.writeObject(subscribeRequest);
                    break;
                case REGISTER: //服务注册
                    RegisterRequest registerRequest = (RegisterRequest) request.getData();

                    ServiceServer.addServiceNode(registerRequest.getServiceName(),registerRequest);
                    System.out.println("注册服务："+registerRequest.getServiceName());
                    List<InetSocketAddress> subscribeList = ServiceServer.getNotifySocket(registerRequest.getServiceName());

                    if(subscribeList!=null) {
                        // 通知相应订阅者
                        System.out.println(subscribeList.toString());
                        for (InetSocketAddress address : subscribeList) {
                            //此处应该线程池异步通知
                            executorService.execute(new Notification(address,registerRequest));
                        }
                    }

                    objectOutputStream.writeObject(registerRequest);
                    break;
                case FIND: //服务发现
                    InvokeRequest invokeRequest = (InvokeRequest) request.getData();
                    System.out.println("服务发现："+invokeRequest);

                    objectOutputStream.writeObject( ServiceServer.getServiceNode(invokeRequest.getServiceName()));
                    break;
                default:
                    break;
            }
        }finally {
            objectOutputStream.close();
        }
    }


}
