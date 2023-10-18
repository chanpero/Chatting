package com.chanper.chatting.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chanper
 * @date 2023/10/18
 */
public class ServiceFactory {
    
    // populate by Config.class
    public static Map<Class<?>, Object> serviceMap = new ConcurrentHashMap<>();
    
    static {
        try {
            Class.forName("com.chanper.chatting.utils.Config");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    public static <T> T getService(Class<T> interfaceClass) {
        return (T) serviceMap.get(interfaceClass);
    }
}
