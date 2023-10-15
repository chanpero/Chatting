package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.GroupChatRequestMessage;
import com.chanper.chatting.message.GroupChatResponseMessage;
import com.chanper.chatting.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chanper
 * @date 2023/10/15
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        GroupSessionFactory.getGroupSession().getMembersChannel(msg.getGroupName()).forEach(channel -> {
            if (!channel.equals(ctx.channel())) {
                channel.writeAndFlush(new GroupChatResponseMessage(msg.getFrom(), msg.getGroupName(), msg.getContent()));
            }
        });
    }
}
