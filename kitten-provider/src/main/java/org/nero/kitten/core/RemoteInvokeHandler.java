package org.nero.kitten.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.nero.kitten.common.core.request.Request;

/**
 * Author neroyang
 * Email  nerosoft@outlook.com
 * Date   2017/9/26
 * Time   下午2:48
 */
public class RemoteInvokeHandler extends SimpleChannelInboundHandler<Request> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {

    }
}
