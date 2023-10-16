package com.chanper.chatting.client;

import com.chanper.chatting.message.AbstractResponseMessage;
import com.chanper.chatting.message.impl.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.chanper.chatting.utils.Constants.COMMA;
import static com.chanper.chatting.utils.Constants.SPACE;

/**
 * @author chanper
 * @date 2023/10/15
 */
@Slf4j
public class BizHandler extends ChannelInboundHandlerAdapter {
    
    // Synchronizer
    static CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);
    static AtomicBoolean LOGIN = new AtomicBoolean(false);
    static AtomicBoolean EXIT = new AtomicBoolean(false);
    
    // Tools
    static Scanner sc = new Scanner(System.in);
    
    // 用户登录
    private static String login(ChannelHandlerContext ctx) {
        System.out.print("用户名：");
        String username = sc.nextLine();
        System.out.print("密码：");
        String password = sc.nextLine();
        
        LoginRequestMessage loginRequest = new LoginRequestMessage(username, password);
        ctx.writeAndFlush(loginRequest);
        return username;
    }
    
    private static void printMenu() {
        System.out.println("================MENU===============");
        System.out.println("send [username] [content]");
        System.out.println("gsend [group_name] [content]");
        System.out.println("gcreate [group_name] [m1,m2,m3...]");
        System.out.println("gmembers [group_name]");
        System.out.println("gjoin [group_name]");
        System.out.println("gquit [group_name]");
        System.out.println("quit");
        System.out.println("================MENU===============");
    }
    
    private static void handleCommand(ChannelHandlerContext ctx, String username) {
        while (true) {
            String[] command = sc.nextLine().split(SPACE);
            try {
                switch (command[0]) {
                    case "send" -> ctx.writeAndFlush(new ChatRequestMessage(username, command[1], command[2]));
                    case "gsend" -> ctx.writeAndFlush(new GroupChatRequestMessage(username, command[1], command[2]));
                    case "gcreate" -> {
                        Set<String> set = new HashSet<>(Arrays.asList(command[2].split(COMMA)));
                        set.add(username);
                        ctx.writeAndFlush(new GroupCreateRequestMessage(command[1], set));
                    }
                    case "gmembers" -> ctx.writeAndFlush(new GroupMembersRequestMessage(command[1]));
                    case "gjoin" -> ctx.writeAndFlush(new GroupJoinRequestMessage(username, command[1]));
                    case "gquit" -> ctx.writeAndFlush(new GroupQuitRequestMessage(username, command[1]));
                    case "quit" -> {
                        ctx.channel().close();
                        return;
                    }
                }
            } catch (Exception e) {
                log.error("命令解析错误！请重试！");
            }
        }
    }
    
    private static void handleResponse(Object msg) {
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage response = (LoginResponseMessage) msg;
            if (response.isSuccess()) {
                System.out.println("登陆成功......");
                LOGIN.set(true);
            } else {
                System.out.println("登陆失败......");
            }
            WAIT_FOR_LOGIN.countDown();
        } else if (msg instanceof ChatResponseMessage) {
            ChatResponseMessage response = (ChatResponseMessage) msg;
            if (response.isSuccess())
                System.out.println(response.getFrom() + ": " + response.getContent());
            else
                System.out.println("消息发送失败, " + response.getReason());
        } else if (msg instanceof GroupChatResponseMessage) {
            GroupChatResponseMessage response = (GroupChatResponseMessage) msg;
            System.out.println(response.getGroupName() + "-" + response.getFrom() + ": " + response.getContent());
        } else if (msg instanceof GroupMembersResponseMessage) {
            ((GroupMembersResponseMessage) msg).getMembers().forEach(member -> System.out.print(member + " "));
            System.out.println();
        } else if (msg instanceof GroupCreateResponseMessage
                || msg instanceof GroupJoinResponseMessage
                || msg instanceof GroupQuitResponseMessage) {
            System.out.println(((AbstractResponseMessage) msg).getReason());
        }
    }
    
    // 连接建立后触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        new Thread(() -> {
            String username = login(ctx);
            
            System.out.println("登陆中......");
            try {
                WAIT_FOR_LOGIN.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 登陆失败：关闭 channel 结束
            if (!LOGIN.get()) {
                ctx.channel().close();
                return;
            }
            
            printMenu();
            handleCommand(ctx, username);
        }, "Command Handler").start();
    }
    
    // 接收响应消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        handleResponse(msg);
    }
    
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.debug("连接已断开, 按任意键退出...");
        EXIT.set(true);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.debug("连接异常断开，error：{}", cause.getMessage());
        EXIT.set(true);
    }
}


