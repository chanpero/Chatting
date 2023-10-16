package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.AbstractResponseMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class ChatResponseMessage extends AbstractResponseMessage {
    
    private String from;
    private String content;
    
    public ChatResponseMessage(String from, String content) {
        this(true, "消息响应成功", from, content);
    }
    
    public ChatResponseMessage(boolean success, String reason, String from, String content) {
        super(success, reason);
        this.from = from;
        this.content = content;
    }
    
    public ChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    
    @Override
    public int getMessageType() {
        return ChatResponseMessage;
    }
}
