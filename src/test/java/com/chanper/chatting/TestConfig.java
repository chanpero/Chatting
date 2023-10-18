package com.chanper.chatting;

import com.chanper.chatting.message.impl.RpcRequestMessage;
import com.chanper.chatting.server.service.HelloService;
import com.chanper.chatting.server.service.ServiceFactory;
import com.chanper.chatting.utils.Config;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    
    @Test
    public void testServiceFactory() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException {
        RpcRequestMessage message = new RpcRequestMessage(
                1,
                "com.chanper.chatting.server.service.HelloService",
                "sayHello",
                String.class,
                new Class[]{String.class},
                new Object[]{"张三"}
        );
        HelloService service = (HelloService) ServiceFactory.getService(Class.forName(message.getInterfaceName()));
        Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterTypes());
        Object invoke = method.invoke(service, message.getParameterValue());
        System.out.println(invoke);
    }
}
