package org.nero.kitten.registercenter.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.nero.kitten.kitten.core.*;
import org.nero.kitten.registercenter.core.dto.OperateType;
import org.nero.kitten.registercenter.core.dto.Service;
import org.nero.kitten.registercenter.core.dto.ServiceOperate;
import org.nero.kitten.registercenter.core.dto.ServiceRequest;
import org.nero.kitten.registercenter.utils.JedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Author :  root
 * Email  :  nerosoft@outlook.com
 * Date   :  16-11-17
 * Time   :  下午4:46
 */
public class DemoClient extends SimpleChannelInboundHandler<KittenResponse> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KittenClient.class);

    private String host;
    private int port;

    private KittenResponse response;

    private final Object obj = new Object();

    public DemoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, KittenResponse response) throws Exception {
        this.response = response;

        synchronized (obj) {
            obj.notifyAll(); // 收到响应，唤醒线程
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("client caught exception", cause);
        ctx.close();
    }

    public KittenResponse send(ServiceRequest request) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new KittenEncoder(ServiceRequest.class)) // 将 RPC 请求进行编码（为了发送请求）
                                    .addLast(new KittenDecoder(KittenResponse.class)) // 将 RPC 响应进行解码（为了处理响应）
                                    .addLast(DemoClient.this); // 使用 RpcClient 发送 RPC 请求
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().writeAndFlush(request).sync();

            synchronized (obj) {
                obj.wait(); // 未收到响应，使线程等待
            }

            if (response != null) {
                future.channel().closeFuture().sync();
            }
            return response;
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        DemoClient client = new DemoClient("127.0.0.1",8888);
        try {
            KittenResponse response =  client.send(
                            new ServiceRequest<ServiceOperate<Service>>(
                                    UUID.randomUUID().toString(),
                                    new ServiceOperate<Service>(
                                        OperateType.SEARCH,
                                        new Service(
                                            UUID.randomUUID().toString(),
                                            "127.0.01",
                                            8080,
                                            "DemoApp",
                                            "com.nero.service.IHelloWorld",
                                            "sayHello",
                                            new Class<?>[]{String.class},
                                            new Object[]{"hello"}
                                        )
                                    )
                            )
                    );

            System.out.println(response.toString());

           } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
