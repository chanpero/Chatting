package com.chanper.chatting.message;

import lombok.Data;

@Data
public class GroupQuitResponseMessage extends AbstractResponseMessage {
    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    
    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
