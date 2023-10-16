package com.chanper.chatting.utils;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author chanper
 * @date 2023/10/15
 */
public class Constants {
    
    public static final String SPACE = " ";
    public static final String COMMA = ",";
    
    public static final Kryo kryo = new Kryo();
    
    static {
        // kryo.register(HashSet.class);
        kryo.setRegistrationRequired(false);
    }
}
