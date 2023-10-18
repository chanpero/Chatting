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
public class RpcResponseMessage extends Message {
    
    // 成功返回值
    private Object returnValue;
    
    // 异常返回值
    private Exception exceptionValue;
    
    
    @Override
    public int getMessageType() {
        return RPC_MESSAGE_TYPE_RESPONSE;
    }
}
