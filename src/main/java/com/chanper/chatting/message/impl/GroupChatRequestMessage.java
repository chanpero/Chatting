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
public class GroupChatRequestMessage extends Message {
    
    private String from;
    private String groupName;
    private String content;
    
    @Override
    public int getMessageType() {
        return GroupChatRequestMessage;
    }
}
