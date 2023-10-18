package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.Message;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author chanper
 * @date 2023/10/18
 */
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class RpcRequestMessage extends Message {
    
    // 调用的接口全限定名，服务端根据它找到实现
    private String interfaceName;
    
    // 调用接口中的方法名
    private String methodName;
    
    // 方法返回类型
    private Class<?> returnType;
    
    // 参数类型列表
    private Class<?>[] parameterTypes;
    
    // 参数列表
    private Object[] parameterValue;
    
    public RpcRequestMessage(int sequenceId, String interfaceName, String methodName, Class<?> returnType, Class<?>[] parameterTypes, Object[] parameterValue) {
        super.setSequenceId(sequenceId);
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }
    
    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_REQUEST;
    }
}
