package com.chanper.chatting;

import com.chanper.chatting.message.impl.LoginRequestMessage;
import com.chanper.chatting.protocol.SerializerAlgorithm;
import org.junit.jupiter.api.Test;

/**
 * @author chanper
 * @date 2023/10/16
 */
public class TestSerializer {
    
    @Test
    public void testSerializer() {
        LoginRequestMessage message = new LoginRequestMessage("chanper", "123");
        
        for (SerializerAlgorithm algorithm : SerializerAlgorithm.values()) {
            System.out.println("===========" + algorithm + "===========");
            
            byte[] serialized = algorithm.serialize(message);
            System.out.println("序列化: " + new String(serialized));
            System.out.println("长度: " + serialized.length);
            
            LoginRequestMessage deserialized = algorithm.deserialize(LoginRequestMessage.class, serialized);
            System.out.println("反序列化: " + deserialized);
            
            
            System.out.println("===========" + algorithm + "===========");
            System.out.println("\n");
        }
    }
    
}
