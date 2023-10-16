package com.chanper.chatting.utils;

import com.chanper.chatting.protocol.SerializerAlgorithm;

import java.io.IOException;
import java.io.InputStream;
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
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
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
