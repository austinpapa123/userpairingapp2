package com.chuyue.usercenter.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Message {
    private Long id;
    private String content;
    private MockUser sender;
    private LocalDateTime timestamp;
}
