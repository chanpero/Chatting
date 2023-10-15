package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.GroupJoinRequestMessage;
import com.chanper.chatting.message.GroupJoinResponseMessage;
import com.chanper.chatting.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chanper
 * @date 2023/10/15
 */
@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        
        if (GroupSessionFactory.getGroupSession().joinMember(groupName, username) != null) {
            GroupSessionFactory.getGroupSession().getMembersChannel(groupName).forEach(channel -> {
                channel.writeAndFlush(new GroupJoinResponseMessage(true, username + "加入[" + groupName + "]成功"));
            });
        } else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false, username + "加入[" + groupName + "]失败"));
        }
    }
}
