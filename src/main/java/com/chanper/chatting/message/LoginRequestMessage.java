package com.chanper.chatting.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {
    private String username;
    private String password;
    
    public LoginRequestMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
