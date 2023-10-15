package com.chanper.chatting.server.handler;


import com.chanper.chatting.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chanper
 * @date 2023/10/15
 */
@Slf4j
@ChannelHandler.Sharable
public class QuitHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 已断开连接", ctx.channel());
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        SessionFactory.getSession().unbind(ctx.channel());
        log.debug("{} 异常断开，{}", ctx.channel(), cause.getMessage());
    }
}
