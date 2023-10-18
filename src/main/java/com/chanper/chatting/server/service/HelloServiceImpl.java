package com.chanper.chatting.server.service;

/**
 * @author chanper
 * @date 2023/10/18
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        // int i / 0;
        return "Hello, " + name;
    }
}
