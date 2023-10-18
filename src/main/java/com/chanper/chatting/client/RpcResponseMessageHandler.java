package com.chanper.chatting.client;

import com.chanper.chatting.message.impl.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chanper
 * @date 2023/10/18
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    
    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        log.debug("{}", msg);
        
        Promise<Object> promise = PROMISES.remove(msg.getSequenceId());
        if (promise != null) {
            Object retVal = msg.getReturnValue();
            Exception expVal = msg.getExceptionValue();
            if (expVal != null) {
                promise.setFailure(expVal);
            } else {
                promise.setSuccess(retVal);
            }
        }
    }
}
