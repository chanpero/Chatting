package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.AbstractResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatResponseMessage extends AbstractResponseMessage {
    
    private String from;
    private String groupName;
    private String content;
    
    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
