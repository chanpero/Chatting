package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.impl.GroupMembersRequestMessage;
import com.chanper.chatting.message.impl.GroupMembersResponseMessage;
import com.chanper.chatting.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;

/**
 * @author chanper
 * @date 2023/10/15
 */
@ChannelHandler.Sharable
public class GroupMembersRequestMessageHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = GroupSessionFactory.getGroupSession().getMembers(groupName);
        ctx.writeAndFlush(new GroupMembersResponseMessage(members));
    }
}
