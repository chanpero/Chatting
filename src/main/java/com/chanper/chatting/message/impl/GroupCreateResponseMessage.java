package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.AbstractResponseMessage;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class GroupCreateResponseMessage extends AbstractResponseMessage {
    
    public GroupCreateResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    
    @Override
    public int getMessageType() {
        return GroupCreateResponseMessage;
    }
}
