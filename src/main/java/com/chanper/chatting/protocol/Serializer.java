package com.chanper.chatting.protocol;

/**
 * @author chanper
 * @date 2023/10/16
 */
public interface Serializer {
    
    // 反序列化
    <T> T deserialize(Class<T> clazz, byte[] bytes);
    
    // 序列化
    <T> byte[] serialize(T object);
    
}
