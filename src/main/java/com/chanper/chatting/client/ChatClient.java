package com.chanper.chatting.client;


import com.chanper.chatting.message.impl.PingMessage;
import com.chanper.chatting.protocol.MessageCodec;
import com.chanper.chatting.protocol.ProcotolFrameDecoder;
import com.chanper.chatting.utils.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatClient {
    
    // Handlers
    static LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    static MessageCodec MESSAGE_CODEC = new MessageCodec();
    
    
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    // 协议处理器
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    // ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    
                    
                    // 心跳处理器,Client处理写空闲事件
                    ch.pipeline().addLast(new IdleStateHandler(0, 3, 0));
                    ch.pipeline().addLast(new ChannelDuplexHandler() {
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                            IdleStateEvent event = (IdleStateEvent) evt;
                            if (event.state() == IdleState.WRITER_IDLE) {
                                // log.debug("3s 没有写数据了，发送一个心跳包");
                                ctx.channel().writeAndFlush(new PingMessage());
                            }
                        }
                    });
                    
                    // 业务应用处理器
                    ch.pipeline().addLast(new BizHandler());
                }
            });
            
            Channel channel = bootstrap.connect("localhost", Config.getServerPort()).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
