package ServiceNotify;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:05
 */
public class ServiceServer<T> implements Server {

    private  int port = 8888;

    private static Map<String,List<InetSocketAddress>> notifyList = new HashMap<String, List<InetSocketAddress>>(20);
    private static Map<String,List<RegisterRequest>> serviceNodeList = new HashMap<String, List<RegisterRequest>>(20);

    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public void start() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            while (true) {
                    executorService.execute(new ServiceTask(serverSocket.accept()));
            }
        }finally {
            if(serverSocket!=null) {
                serverSocket.close();
            }
        }

    }

    public static void addNotify(String serviceName,InetSocketAddress address){
        System.out.println("添加订阅："+serviceName+" ："+address);
        if(notifyList.get(serviceName)!=null) {
            notifyList.get(serviceName).add(address);
        }else{
            List<InetSocketAddress> list = new ArrayList<InetSocketAddress>(10);
            list.add(address);
            notifyList.put(serviceName,list);
        }
    }
    public static List<InetSocketAddress> getNotifySocket(String serviceName){
        System.out.println("消费者："+notifyList.toString());
        return notifyList.get(serviceName);
    }

    public static void addServiceNode(String serviceName,RegisterRequest registerRequest){
        if(serviceNodeList.get(serviceName)!=null){
            serviceNodeList.get(serviceName).add(registerRequest);
        }else{
            List<RegisterRequest> list = new ArrayList<RegisterRequest>(10);
            list.add(registerRequest);
            serviceNodeList.put(serviceName,list);
        }
    }
    public static List<RegisterRequest> getServiceNode(String serviceName){
        return serviceNodeList.get(serviceName);
    }

    class ServiceTask implements Runnable{
        private Socket socket;
        public ServiceTask(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            ObjectInputStream inputStream = null;

            try{
                inputStream = new ObjectInputStream(socket.getInputStream());
                Request<T> request = (Request<T>) inputStream.readObject();
                ServiceHandler handler  = new ServiceHandler(socket);
                handler.handle(request);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        executorService.shutdown();
    }

    public ServiceServer(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Map<String, List<InetSocketAddress>> getNotifyList() {
        return notifyList;
    }

    public static void setNotifyList(Map<String, List<InetSocketAddress>> notifyList) {
        ServiceServer.notifyList = notifyList;
    }


}
