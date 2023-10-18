package com.chanper.chatting.client;

import com.chanper.chatting.message.impl.RpcRequestMessage;
import com.chanper.chatting.protocol.MessageCodec;
import com.chanper.chatting.protocol.ProcotolFrameDecoder;
import com.chanper.chatting.server.service.HelloService;
import com.chanper.chatting.utils.Config;
import com.chanper.chatting.utils.SequenceIdGenerator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author chanper
 * @date 2023/10/18
 */
@Slf4j
public class RpcClient {
    
    private static final Object LOCK = new Object();
    static LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
    static MessageCodec MESSAGE_CODEC = new MessageCodec();
    static RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
    private static volatile Channel channel = null;
    
    public static void main(String[] args) {
        HelloService service = getProxyService(HelloService.class);
        
        System.out.println(service.sayHello("zhangsan"));
        System.out.println(service.sayHello("lisi"));
        System.out.println(service.sayHello("chanper"));
    }

    private static <T> T getProxyService(Class<T> serviceClass) {
        Class<?>[] interfaces = new Class<?>[]{serviceClass};
        
        Object proxyObject = Proxy.newProxyInstance(serviceClass.getClassLoader(), interfaces, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 构造RPC请求的消息对象
                int sequenceId = SequenceIdGenerator.nextId();
                RpcRequestMessage msg = new RpcRequestMessage(sequenceId, serviceClass.getName(), method.getName(), method.getReturnType(), method.getParameterTypes(), args);
                
                // 发送RPC请求
                getChannel().writeAndFlush(msg);
                
                // 创建接收结果的Promise，指定异步接收结果的线程
                DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
                RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);
                
                // 此处暂时同步等待结果
                promise.await();
                if (promise.isSuccess()) {
                    return promise.getNow();
                } else {
                    throw new RuntimeException(promise.cause());
                }
            }
        });
        
        return (T) proxyObject;
    }

    private static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        
        synchronized (LOCK) {
            if (channel != null) {
                return channel;
            }
            initChannel();
            return channel;
        }
    }
    
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast(RPC_HANDLER);
                }
            });
            
            channel = bootstrap.connect("localhost", Config.getServerPort()).sync().channel();
            channel.closeFuture().addListener(future -> group.shutdownGracefully());
        } catch (Exception e) {
            log.error("client error", e);
        }
    }
}
