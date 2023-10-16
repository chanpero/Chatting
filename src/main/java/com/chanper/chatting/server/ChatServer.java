package com.chanper.chatting.server;


import com.chanper.chatting.message.impl.PingMessage;
import com.chanper.chatting.message.impl.PongMessage;
import com.chanper.chatting.protocol.MessageCodec;
import com.chanper.chatting.protocol.ProcotolFrameDecoder;
import com.chanper.chatting.server.handler.*;
import com.chanper.chatting.utils.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    
    // Handlers
    static LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    static MessageCodec MESSAGE_CODEC = new MessageCodec();
    static LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
    static ChatRequestMessageHandler CHAT_HANDLER = new ChatRequestMessageHandler();
    static GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
    static GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
    static GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
    static GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
    static GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();
    static QuitHandler QUIT_HANDLER = new QuitHandler();
    
    
    public static void main(String[] args) {
        
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 协议处理器
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    
                    // 心跳处理器, Server处理读空闲事件
                    ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.READER_IDLE) {
                                log.debug(ctx.channel() + "该连接 5s 没有可读数据...");
                                ctx.channel().close();
                            }
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if (msg instanceof PingMessage) {
                                ctx.writeAndFlush(new PongMessage());
                            }
                            super.channelRead(ctx, msg);
                        }
                    });
                    
                    
                    // 业务应用处理器
                    ch.pipeline().addLast(LOGIN_HANDLER);
                    ch.pipeline().addLast(CHAT_HANDLER);
                    ch.pipeline().addLast(GROUP_CREATE_HANDLER);
                    ch.pipeline().addLast(GROUP_MEMBERS_HANDLER);
                    ch.pipeline().addLast(GROUP_JOIN_HANDLER);
                    ch.pipeline().addLast(GROUP_CHAT_HANDLER);
                    ch.pipeline().addLast(GROUP_QUIT_HANDLER);
                    ch.pipeline().addLast(QUIT_HANDLER);
                }
            });
            
            Channel channel = serverBootstrap.bind(Config.getServerPort()).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
