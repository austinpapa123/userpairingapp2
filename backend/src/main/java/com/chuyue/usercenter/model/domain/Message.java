package com.chuyue.usercenter.model.domain;

import com.chuyue.usercenter.common.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Message {
    private Long id;
    private MessageType messageType;
    private String roomName;
    private String content;
    private MockUser sender;
    private LocalDateTime timestamp;
}
