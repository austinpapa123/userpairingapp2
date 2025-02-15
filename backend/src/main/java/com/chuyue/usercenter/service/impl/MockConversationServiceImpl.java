package com.chuyue.usercenter.service.impl;

import com.chuyue.usercenter.model.domain.Conversation;
import com.chuyue.usercenter.model.domain.Message;
import com.chuyue.usercenter.model.domain.MockUser;
import com.chuyue.usercenter.service.MockConversationService;
import lombok.extern.slf4j.Slf4j;
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

    private final List<MockUser> mockUsers = List.of(
            MockUser.builder().id(1L).username("alice").avatar("src/assets/images/cover7.png").role("regular").build(),
            MockUser.builder().id(2L).username("bob").avatar("src/assets/images/cover8.png").role("regular").build()
    );

    private final Map<Long, Conversation> conversations = new HashMap<>(); // Maps conversation ID to Conversation object

    public MockConversationServiceImpl() {
        // Create a sample conversation between Alice and Bob
        List<MockUser> participants = List.of(mockUsers.get(0), mockUsers.get(1));
        Conversation conversation = new Conversation();
        conversation.setId(1L);
        conversation.setRoomName("alice_bob");
        conversation.setType("private");
        conversation.setParticipants(participants);

        // Add some sample messages
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1L, "Hey Bob, how are you?", mockUsers.get(0), LocalDateTime.now().minusMinutes(5)));
        messages.add(new Message(2L, "I'm good, Alice! How about you?", mockUsers.get(1), LocalDateTime.now().minusMinutes(4)));
        conversation.setMessages(messages);

        conversations.put(conversation.getId(), conversation); // Store in memory
    }

    @Override
    public List<Conversation> getConversationsByUser(String username) {
        return conversations.values().stream()
                .filter(conversation -> conversation.getParticipants().stream()
                        .anyMatch(user -> user.getUsername().equals(username)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessagesForConversation(Long conversationId) {
        Conversation conversation = conversations.get(conversationId);
        return conversation != null ? conversation.getMessages() : new ArrayList<>();
    }
}
