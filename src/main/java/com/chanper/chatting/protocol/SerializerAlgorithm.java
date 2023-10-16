package com.chanper.chatting.protocol;

import com.chanper.chatting.utils.Constants;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.google.gson.Gson;

import java.io.*;

/**
 * @author chanper
 * @date 2023/10/16
 */
public enum SerializerAlgorithm implements Serializer {
    // Java Serializer
    Java {
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                return (T) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException("SerializerAlgorithm.Java 反序列化错误", e);
            }
        }
        
        @Override
        public <T> byte[] serialize(T object) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                new ObjectOutputStream(bos).writeObject(object);
                return bos.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException("SerializerAlgorithm.Java 序列化错误", e);
            }
        }
    },
    
    
    // Json Serializer
    Json {
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            return new Gson().fromJson(new String(bytes), clazz);
        }
        
        @Override
        public <T> byte[] serialize(T object) {
            return new Gson().toJson(object).getBytes();
        }
    },
    
    
    // Kryo Serializer
    Kryo {
        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            Kryo kryo = Constants.kryo;
            kryo.register(clazz);
            
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            Input input = new Input(bis);
            T object = kryo.readObject(input, clazz);
            input.close();
            return object;
        }
        
        @Override
        public <T> byte[] serialize(T object) {
            Kryo kryo = Constants.kryo;
            kryo.register(object.getClass());
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Output output = new Output(bos);
            kryo.writeObject(output, object);
            output.close();
            return bos.toByteArray();
        }
    };
    
    
    public static SerializerAlgorithm getByType(int type) {
        SerializerAlgorithm[] algorithms = SerializerAlgorithm.values();
        if (type < 0 || type >= algorithms.length) {
            throw new IllegalArgumentException("SerializerAlgorithm 类型范围错误");
        }
        return algorithms[type];
    }
    
}
