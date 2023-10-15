package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.GroupCreateRequestMessage;
import com.chanper.chatting.message.GroupCreateResponseMessage;
import com.chanper.chatting.server.session.Group;
import com.chanper.chatting.server.session.GroupSession;
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
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group != null) {
            ctx.writeAndFlush(new GroupCreateResponseMessage(true, "群聊[" + groupName + "]创建成功"));
            groupSession.getMembersChannel(groupName).forEach(channel -> {
                if (!channel.equals(ctx.channel()))
                    channel.writeAndFlush(new GroupCreateResponseMessage(true, "您被拉入群聊[" + groupName + "]"));
            });
        } else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false, "群聊[" + groupName + "]创建失败"));
        }
        
    }
}
