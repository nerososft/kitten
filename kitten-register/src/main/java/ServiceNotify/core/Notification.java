package ServiceNotify.core;

import org.nero.kitten.common.core.request.RegisterRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   下午3:02
 */
public class Notification  implements  Runnable{

    private InetSocketAddress address;
    private RegisterRequest registerRequest;

    public Notification(InetSocketAddress address, RegisterRequest registerRequest) {
        this.address =address;
        this.registerRequest = registerRequest;
    }

    public void run() {
        //通知
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;

        try{
            socket = new Socket();
            socket.connect(address);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(registerRequest);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
