package com.chanper.chatting.server.handler;

import com.chanper.chatting.message.impl.RpcRequestMessage;
import com.chanper.chatting.message.impl.RpcResponseMessage;
import com.chanper.chatting.server.service.HelloService;
import com.chanper.chatting.server.service.ServiceFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author chanper
 * @date 2023/10/18
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage msg) throws Exception {
        RpcResponseMessage response = new RpcResponseMessage();
        response.setSequenceId(msg.getSequenceId());
        
        try {
            HelloService service = (HelloService) ServiceFactory.getService(Class.forName(msg.getInterfaceName()));
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object ret = method.invoke(service, msg.getParameterValue());
            response.setReturnValue(ret);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InvocationTargetException |
                 IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
            String exp = e.getCause().getMessage();
            response.setExceptionValue(new Exception("远程调用出错: " + exp));
        }
        
        ctx.writeAndFlush(response);
    }
}
