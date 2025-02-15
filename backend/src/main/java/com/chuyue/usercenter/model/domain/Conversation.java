package com.chuyue.usercenter.model.domain;

import lombok.Data;

import java.util.List;

@Data
public class Conversation {
    private Long id;
    private String roomName; // e.g., alice_bob
    private String type; // "private" or "group"
    private String avatar; // Can be a default avatar or a specific one
    private List<MockUser> participants;
    private List<Message> messages;
}
