package org.nero.kitten.kitten.core;

import net.sf.cglib.proxy.InvocationHandler;
import net.sf.cglib.proxy.Proxy;
import org.nero.kitten.kitten.core.KittenClient;
import org.nero.kitten.kitten.core.KittenRequest;
import org.nero.kitten.kitten.core.KittenResponse;
import org.nero.kitten.kitten.core.ServiceDiscovery;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Author :  root
 * Email  :  nerosoft@outlook.com
 * Date   :  16-11-17
 * Time   :  下午4:45
 */
public class KittenProxy {
    private String serverAddress;
    private ServiceDiscovery serviceDiscovery;

    public KittenProxy(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public KittenProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<?> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        KittenRequest request = new KittenRequest(); // 创建并初始化 RPC 请求
                        request.setRequestId(UUID.randomUUID().toString());
                        request.setClassName(method.getDeclaringClass().getName());
                        request.setMethodName(method.getName());
                        request.setParameterTypes(method.getParameterTypes());
                        request.setParameters(args);

                        if (serviceDiscovery != null) {
                            serverAddress = serviceDiscovery.discover(); // 发现服务
                        }

                        String[] array = serverAddress.split(":");
                        String host = array[0];
                        int port = Integer.parseInt(array[1]);

                        KittenClient client = new KittenClient(host, port); // 初始化 RPC 客户端
                        KittenResponse response = client.send(request); // 通过 RPC 客户端发送 RPC 请求并获取 RPC 响应

                        if (response.isError()) {
                            throw response.getError();
                        } else {
                            return response.getResult();
                        }
                    }
                }
        );
    }
}
