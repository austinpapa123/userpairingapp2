package com.chuyue.usercenter.controller;

import com.chuyue.usercenter.common.BaseResponse;
import com.chuyue.usercenter.common.ResultUtils;
import com.chuyue.usercenter.model.domain.Conversation;
import com.chuyue.usercenter.model.domain.Message;
import com.chuyue.usercenter.service.MockConversationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/convo")
@Slf4j
public class MockConversationController {

    @Resource
    private MockConversationService mockConversationService;

    @GetMapping("/fetch_conversations")
    public BaseResponse<List<Conversation>> fetchConversations(@RequestParam String username) {
        List<Conversation> conversations = mockConversationService.getConversationsByUser(username);
        return ResultUtils.success(conversations);
    }

    @GetMapping("/fetch_message_history")
    public BaseResponse<List<Message>> fetchMessages(@RequestParam Long conversationId) {
        List<Message> messages = mockConversationService.getMessagesForConversation(conversationId);
        return ResultUtils.success(messages);
    }
}
