package com.chanper.chatting.message.impl;

import com.chanper.chatting.message.Message;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
