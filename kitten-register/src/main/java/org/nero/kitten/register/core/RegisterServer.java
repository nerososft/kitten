package org.nero.kitten.register.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.nero.kitten.common.core.KittenDecoder;
import org.nero.kitten.common.core.KittenEncoder;
import org.nero.kitten.common.core.KittenResponse;
import org.nero.kitten.register.core.dto.ServiceRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/13
 * Time   下午2:18
 */
public class RegisterServer {



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
                                    .addLast(new KittenDecoder(ServiceRequest.class))
                                    .addLast(new KittenEncoder(KittenResponse.class))
                                    .addLast(new ServiceRegisterHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);


            // 绑定端口，开始接收进来的连接
            ChannelFuture f = serverBootstrap.bind(8888).sync(); // (7)


            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            boosGroup.shutdownGracefully();
        }
    }


}
