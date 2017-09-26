package ServiceNotify.core;

import org.nero.kitten.common.core.request.RegisterRequest;
import org.nero.kitten.common.core.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   下午3:09
 */
public class ConsumerServer implements Server {

    private  int port = 8889;
    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public ConsumerServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
            while (true) {
                executorService.execute(new ConsumerTask(serverSocket.accept()));
            }
        }finally {
            if(serverSocket!=null) {
                serverSocket.close();
            }
        }
    }
    class ConsumerTask implements Runnable{
        private Socket socket;
        public ConsumerTask(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            ObjectInputStream inputStream = null;

            try{
                inputStream = new ObjectInputStream(socket.getInputStream());
                RegisterRequest request = (RegisterRequest) inputStream.readObject();
                System.out.println("服务上线通知："+request.toString());
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
}
