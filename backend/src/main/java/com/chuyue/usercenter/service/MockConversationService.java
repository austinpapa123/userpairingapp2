package com.chuyue.usercenter.service;

import com.chuyue.usercenter.model.domain.Conversation;
import com.chuyue.usercenter.model.domain.Message;

import java.util.List;

public interface MockConversationService {

    List<Conversation> getConversationsByUser(String username);
    List<Message> getMessagesForConversation(Long conversationId);

}
