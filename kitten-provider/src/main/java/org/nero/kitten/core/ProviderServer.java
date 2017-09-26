package org.nero.kitten.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.collections4.MapUtils;
import org.nero.kitten.common.core.*;
import org.nero.kitten.common.core.dto.Service;
import org.nero.kitten.common.core.request.RegisterRequest;
import org.nero.kitten.common.core.request.Request;
import org.nero.kitten.common.core.request.ServiceRequestType;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/26
 * Time   下午1:54
 */
public class ProviderServer implements Server, ApplicationContextAware, InitializingBean {

    private int port = 8889;
    private String host = "localhost";

    private ChannelFuture future;

    public ProviderServer(int port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException {
        EventLoopGroup bossEventLoop = new NioEventLoopGroup();
        EventLoopGroup workerLoop = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoop, workerLoop)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new KittenDecoder(Request.class))
                                    .addLast(new KittenEncoder(Request.class))
                                    .addLast(new RemoteInvokeHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            future = serverBootstrap.bind(host, port).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerLoop.shutdownGracefully();
            bossEventLoop.shutdownGracefully();
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
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(KittenService.class); // 获取所有带有 RpcService 注解的 Spring Bean
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            KittenClient client = new KittenClient("localhost", 8888);
            for (Object serviceBean : serviceBeanMap.values()) {
                String interfaceName = serviceBean.getClass().getAnnotation(KittenService.class).value().getName();

                //此处注册服务
                //handlerMap.put(interfaceName, serviceBean);
                try {
                    client.send(new Request<RegisterRequest>(
                            UUID.randomUUID().toString(),
                            ServiceRequestType.REGISTER,
                            new RegisterRequest(
                                    "localhost",
                                    8889,
                                    "",
                                    null,
                                    null,
                                    null
                            )
                    ));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
