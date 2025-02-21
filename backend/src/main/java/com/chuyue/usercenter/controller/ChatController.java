package com.chuyue.usercenter.controller;

import com.chuyue.usercenter.model.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * handling private chat message
     * @param chatRoomId "userID1_userID2" with userID1 < userID2
     * @param message
     */
    @MessageMapping("/private/{chatRoomId}")
    public void handlePrivateChatMessage(@DestinationVariable String chatRoomId, Message message) {
        // Logic to handle the message, such as broadcasting it to users subscribed to this chat room
        simpMessagingTemplate.convertAndSend("/topic/private/" + chatRoomId, message);
    }
}
