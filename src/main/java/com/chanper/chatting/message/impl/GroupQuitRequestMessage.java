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
public class GroupQuitRequestMessage extends Message {
    
    private String username;
    private String groupName;
    
    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
