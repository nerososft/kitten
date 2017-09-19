package ServiceNotify.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/19
 * Time   上午11:06
 */
public class Client<T,R> {

    private InetSocketAddress address;

    public Client( InetSocketAddress address) {

        this.address =address;
    }

    public R send(T data) throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;
        Socket socket = null;
        try{
            socket = new Socket();
            socket.connect(address);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(data);

            inputStream = new ObjectInputStream(socket.getInputStream());
            R result = (R)inputStream.readObject();

            return result;
        }finally {
            outputStream.close();
            inputStream.close();
            socket.close();
        }
    }

    public R subscribe(T subscribeRequestRequest) throws IOException, ClassNotFoundException {
        return this.send(subscribeRequestRequest);
    }

    public R registerService(T registerRequestRequest) throws IOException, ClassNotFoundException {
        return this.send(registerRequestRequest);
    }

    public R findService(T invokeRequestRequest) throws IOException, ClassNotFoundException {
        return this.send(invokeRequestRequest);
    }

}
