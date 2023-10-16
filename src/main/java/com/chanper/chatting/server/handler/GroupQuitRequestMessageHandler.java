package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.impl.GroupQuitRequestMessage;
import com.chanper.chatting.message.impl.GroupQuitResponseMessage;
import com.chanper.chatting.server.session.GroupSession;
import com.chanper.chatting.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chanper
 * @date 2023/10/15
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        if (groupSession.removeMember(groupName, username) != null) {
            groupSession.getMembersChannel(groupName).forEach(channel -> {
                channel.writeAndFlush(new GroupQuitResponseMessage(true, username + "退出群聊[" + groupName + "]"));
            });
            
            if (groupSession.getMembers(groupName).size() == 0) {
                groupSession.removeGroup(groupName);
            }
            
            ctx.writeAndFlush(new GroupQuitResponseMessage(true, "您退出群聊[" + groupName + "]"));
        } else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false, "群聊不存在，退出失败"));
        }
    }
}
