package org.nero.kitten.register.core;

import main.java.core.Server;
import main.java.core.request.RegisterRequest;
import main.java.core.request.Request;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import main.java.core.KittenDecoder;
import main.java.core.KittenEncoder;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/13
 * Time   下午2:18
 */
public class RegisterServer implements Server {

    private int port = 8888;
    private Boolean isRunning = true;
    private ChannelFuture future;


    private static Map<String,List<InetSocketAddress>> notifyList = new HashMap<String, List<InetSocketAddress>>(20);
    private static Map<String,List<RegisterRequest>> serviceNodeList = new HashMap<String, List<RegisterRequest>>(20);


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


    public RegisterServer(int port) {
        this.port = port;
    }

    public void start(){
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new KittenDecoder(Request.class))

                                    .addLast(new KittenEncoder(RegisterRequest.class))
                                    .addLast(new ServiceRegisterHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);


            // 绑定端口，开始接收进来的连接
            future = serverBootstrap.bind(port).sync(); // (7)



            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }

    public void stop() throws InterruptedException {
        future.channel().closeFuture().sync();
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Boolean getRunning() {
        return isRunning;
    }

    public void setRunning(Boolean running) {
        isRunning = running;
    }
}
