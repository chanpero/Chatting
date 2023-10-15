package com.chanper.chatting.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {
    private String from;
    private String groupName;
    private String content;
    
    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }
    
    public GroupChatResponseMessage(String from, String content) {
        this.from = from;
        this.content = content;
    }
    
    public GroupChatResponseMessage(String from, String groupName, String content) {
        this.from = from;
        this.groupName = groupName;
        this.content = content;
    }
    
    @Override
    public int getMessageType() {
        return GroupChatResponseMessage;
    }
}
