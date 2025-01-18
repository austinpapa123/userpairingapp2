package com.chuyue.usercenter.model.domain;

import com.chuyue.usercenter.common.MessageType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {

    private String content;
    private String sender;
    private MessageType messageType;
    private String avatarUrl;
}
