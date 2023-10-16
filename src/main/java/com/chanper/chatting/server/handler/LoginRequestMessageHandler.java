package com.chanper.chatting.server.handler;


import com.chanper.chatting.message.impl.LoginRequestMessage;
import com.chanper.chatting.message.impl.LoginResponseMessage;
import com.chanper.chatting.server.service.UserServiceFactory;
import com.chanper.chatting.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author chanper
 * @date 2023/10/14
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String username = msg.getUsername();
        String password = msg.getPassword();
        
        LoginResponseMessage response;
        if (UserServiceFactory.getUserService().login(username, password)) {
            SessionFactory.getSession().bind(ctx.channel(), username);
            response = new LoginResponseMessage(true, "登录成功");
        } else {
            response = new LoginResponseMessage(false, "用户名/密码不正确");
        }
        
        ctx.writeAndFlush(response);
    }
}
