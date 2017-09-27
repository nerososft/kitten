package org.nero.kitten.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import org.nero.kitten.common.core.*;
import org.nero.kitten.common.core.request.Request;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/27
 * Time   上午10:44
 */
public class ConsumerServer implements ApplicationContextAware,InitializingBean,Server {

    private String host ="localhost";
    private int port = 8890;
    private ChannelFuture future;


    public ConsumerServer(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public void start() throws IOException {
        NioEventLoopGroup bossLoop = new NioEventLoopGroup();
        NioEventLoopGroup workerLoop = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossLoop,workerLoop)
                    .channel(ServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new KittenDecoder(Request.class))
                                    .addLast(new KittenEncoder(KittenResponse.class))
                                    .addLast(new NotifyHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            future = serverBootstrap.bind(host,port).sync();

            future.channel().closeFuture().sync();//不关闭

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerLoop.shutdownGracefully();
            bossLoop.shutdownGracefully();
        }
    }

    @Override
    public void stop() throws InterruptedException {
        future.channel().close().sync();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //读取服务消费端配置并监听

    }
}
