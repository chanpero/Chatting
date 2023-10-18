package com.chanper.chatting.utils;

import com.chanper.chatting.protocol.SerializerAlgorithm;
import com.chanper.chatting.server.service.ServiceFactory;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * @author chanper
 * @date 2023/10/16
 */
public abstract class Config {
    
    static Properties properties;
    
    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
            
            // 加载 RPC 服务列表
            loadServiceList();
            
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    /**
     * 从配置文件中读取远程服务的实现，充当注册中心
     */
    private static void loadServiceList() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (String serviceName : properties.stringPropertyNames()) {
            if (serviceName.endsWith("Service")) {
                Class<?> interfaceClass = Class.forName(serviceName);
                Class<?> implClass = Class.forName(properties.getProperty(serviceName));
                ServiceFactory.serviceMap.put(interfaceClass, implClass.getDeclaredConstructor().newInstance());
            }
        }
    }
    
    public static int getServerPort() {
        String port = properties.getProperty("server.port");
        if (port == null) {
            return 8080;
        } else {
            return Integer.parseInt(port);
        }
    }
    
    public static SerializerAlgorithm getSerializer() {
        String serializer = properties.getProperty("serializer.algorithm");
        if (serializer == null) {
            return SerializerAlgorithm.Java;
        } else {
            return SerializerAlgorithm.valueOf(serializer);
        }
    }
}
