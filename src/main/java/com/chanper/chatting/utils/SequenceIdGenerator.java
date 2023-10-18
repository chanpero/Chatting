package com.chanper.chatting.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chanper
 * @date 2023/10/18
 */
public abstract class SequenceIdGenerator {
    
    private static final AtomicInteger id = new AtomicInteger();
    
    public static int nextId() {
        return id.incrementAndGet();
    }
}
