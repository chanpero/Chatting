package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.impl.ChatRequestMessage;
import com.chanper.chatting.message.impl.ChatResponseMessage;
import com.chanper.chatting.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chanper
 * @date 2023/10/14
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        
        if (channel != null) {   // 发送目标在线
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
        } else {
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方不在线"));
        }
    }
}
