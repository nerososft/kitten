package org.nero.kitten.register.core;

import ServiceNotify.core.Notification;
import main.java.core.request.InvokeRequest;
import main.java.core.request.RegisterRequest;
import main.java.core.request.Request;
import main.java.core.request.SubscribeRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import main.java.core.KittenResponse;
import main.java.core.SerializationUtil;
import main.java.core.dto.Service;
import org.nero.kitten.register.utils.JedisUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/13
 * Time   下午2:33
 */
public class ServiceRegisterHandler extends SimpleChannelInboundHandler<Request> {


    ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
        KittenResponse response = new KittenResponse();
        response.setRequestId(request.getRequestID());

        switch (request.getServiceRequestType()) {
            case SUBSCRIBE: //消费者订阅

                SubscribeRequest subscribeRequest = (SubscribeRequest) request.getData();
                System.out.println("服务订阅：" + subscribeRequest);

                for (String serviceName : subscribeRequest.getServiceList()) {
                    System.out.println(serviceName);
                    RegisterServer.addNotify(serviceName, new InetSocketAddress(subscribeRequest.getIp(), subscribeRequest.getPort())); //将客户端添加至订阅列表
                }
                response.setResult(subscribeRequest);
                break;
            case REGISTER: //服务注册
                RegisterRequest registerRequest = (RegisterRequest) request.getData();

                RegisterServer.addServiceNode(registerRequest.getServiceName(), registerRequest);
                System.out.println("注册服务：" + registerRequest.getServiceName());
                List<InetSocketAddress> subscribeList = RegisterServer.getNotifySocket(registerRequest.getServiceName());

                if (subscribeList != null) {
                    // 通知相应订阅者
                    System.out.println(subscribeList.toString());
                    for (InetSocketAddress address : subscribeList) {
                        //此处应该线程池异步通知
                        executorService.execute(new Notification(address, registerRequest));
                    }
                }
                response.setResult(registerRequest);
                break;
            case FIND: //服务发现
                InvokeRequest invokeRequest = (InvokeRequest) request.getData();
                System.out.println("服务发现：" + invokeRequest);
                response.setResult(RegisterServer.getServiceNode(invokeRequest.getServiceName()));
                break;
            default:
                break;
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }

    private Object searchService(String serviceName) {
        System.out.println("search service " + serviceName + " !");

        return SerializationUtil.deserialize(
                JedisUtils.getInstance().strings().get(
                        serviceName.getBytes()
                ),
                Service.class
        );
    }

    String upgradeService(Service service) {

        System.out.println("service register!");

        return JedisUtils.getInstance().strings().set(
                service.getServiceName(),
                SerializationUtil.serialize(service)
        );
    }

}
