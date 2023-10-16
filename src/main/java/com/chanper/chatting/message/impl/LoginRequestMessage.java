package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestMessage extends Message {
    
    private String username;
    private String password;
    
    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
