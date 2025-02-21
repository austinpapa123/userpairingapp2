package com.chuyue.usercenter.service.impl;

import com.chuyue.usercenter.common.MessageType;
import com.chuyue.usercenter.model.domain.Conversation;
import com.chuyue.usercenter.model.domain.Message;
import com.chuyue.usercenter.model.domain.MockUser;
import com.chuyue.usercenter.service.MockConversationService;
import com.chuyue.usercenter.service.MockUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MockConversationServiceImpl implements MockConversationService {

    @Resource
    private MockUserService mockUserService; // Inject MockUserService

    private final Map<Long, Conversation> conversations = new HashMap<>(); // Maps conversation ID to Conversation object

    private boolean isInitialized = false; // Flag to track initialization

    @Override
    public void initializeConversation() {
        if (isInitialized) return; // Skip if already initialized

        List<MockUser> participants = mockUserService.getAllMockUsers();
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setRoomName("alice_bob");
        conversation.setType("private");
        conversation.setParticipants(participants);

        // Add some sample messages
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1L,
                MessageType.CHAT,
                "alice_bob",
                "Hey Bob, how are you?",
                participants.get(0),
                LocalDateTime.now().minusMinutes(5)));
        messages.add(new Message(2L,
                MessageType.CHAT,
                "alice_bob",
                "I'm good, Alice! How about you?",
                participants.get(1),
                LocalDateTime.now().minusMinutes(4)));
        conversation.setMessages(messages);

        conversations.put(conversation.getId(), conversation); // Store in memory
    }

    @Override
    public List<Conversation> getConversationsByUser(String username) {

        // Ensure conversations are initialized
        initializeConversation();

        return conversations.values().stream()
                .filter(conversation -> conversation.getParticipants().stream()
                        .anyMatch(user -> user.getUsername().equals(username)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesForConversation(Long conversationId) {

        // Ensure conversations are initialized
        initializeConversation();

        Conversation conversation = conversations.get(conversationId);
        return conversation != null ? conversation.getMessages() : new ArrayList<>();
    }
}

