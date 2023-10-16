package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.AbstractResponseMessage;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupQuitResponseMessage extends AbstractResponseMessage {
    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    
    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
