package com.chanper.chatting;

import com.chanper.chatting.utils.Config;
import org.junit.jupiter.api.Test;

/**
 * @author chanper
 * @date 2023/10/16
 */
public class TestConfig {
    
    @Test
    public void testConfig() {
        System.out.println(Config.getServerPort());
        System.out.println(Config.getSerializer());
    }
}
