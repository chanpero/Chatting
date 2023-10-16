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
public class ChatRequestMessage extends Message {
    
    private String from;
    private String to;
    private String content;
    
    @Override
    public int getMessageType() {
        return ChatRequestMessage;
    }
}
