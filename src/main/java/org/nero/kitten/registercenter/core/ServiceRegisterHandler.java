package org.nero.kitten.registercenter.core;

import com.google.gson.Gson;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.nero.kitten.kitten.core.KittenResponse;
import org.nero.kitten.kitten.core.SerializationUtil;
import org.nero.kitten.registercenter.core.dto.Service;
import org.nero.kitten.registercenter.core.dto.ServiceOperate;
import org.nero.kitten.registercenter.core.dto.ServiceRequest;
import org.nero.kitten.registercenter.utils.JedisUtils;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/13
 * Time   下午2:33
 */
public class ServiceRegisterHandler extends SimpleChannelInboundHandler<ServiceRequest<ServiceOperate<Service>>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ServiceRequest<ServiceOperate<Service>> service) throws Exception {
        KittenResponse  response = new KittenResponse();
        response.setRequestId(service.getRequestId());
        try {
            Object result = null;
            switch (service.getData().getOperateType()){
                case REGISTER:
                     result = upgradeService(service.getData().getData());
                    break;
                case SEARCH:
                    break;
                case MANAGER:
                    break;
                default:
                    return;
            }
            response.setResult(result);


        } catch (Throwable t) {
            response.setError(t);
        }
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    String upgradeService(Service service){

        System.out.println("service register!");

        Gson gson = new Gson();
        return JedisUtils.getInstance().strings().set(service.getServiceName(), SerializationUtil.serialize(service));//gson.toJson(service,Service.class));
    }

}
