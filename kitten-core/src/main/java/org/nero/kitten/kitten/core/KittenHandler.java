package org.nero.kitten.kitten.core;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.nero.kitten.common.core.KittenRequest;
import org.nero.kitten.common.core.KittenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Author :  root
 * Email  :  nerosoft@outlook.com
 * Date   :  16-11-17
 * Time   :  下午4:40
 */
public class KittenHandler extends SimpleChannelInboundHandler<KittenRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KittenHandler.class);

    private final Map<String, Object> handlerMap;

    private Map<String,FastMethod> methodCache = new HashMap<>();

    public KittenHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, KittenRequest request) throws Exception {
        KittenResponse response = new KittenResponse();
        response.setRequestId(request.getRequestId());
        try {
            Object result = handle(request);
            response.setResult(result);
        } catch (Throwable t) {
            response.setError(t);
        }
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(KittenRequest request) throws Throwable {
        String className = request.getClassName();
        Object serviceBean = handlerMap.get(className);

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = null;
        if((serviceFastMethod = methodCache.get(methodName))!=null){
            return serviceFastMethod.invoke(serviceBean, parameters);
        }
        serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        methodCache.put(methodName,serviceFastMethod);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("server  caught exception", cause);
        ctx.close();
    }

}
