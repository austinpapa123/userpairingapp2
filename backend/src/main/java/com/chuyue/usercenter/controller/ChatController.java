package com.chuyue.usercenter.controller;

import com.chuyue.usercenter.model.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping("/chatcontroller/test")
    public String test(String payload) {
        return payload;
    }

    /**
     * broadcasting channel request
     * @param chatMessage
     * @return
     */
    @MessageMapping("/sendMessage")
    @SendTo("/topic/testing")
    public ChatMessage sendMessage(
        @Payload ChatMessage chatMessage
    ) {
        return chatMessage;
    }

    /**
     * New user join notification
     * @param chatMessage
     * @param simpMessageHeaderAccessor
     * @return
     */
    @MessageMapping("/joinUser")
    @SendTo("/topic/testing")
    public ChatMessage joinUser(
        @Payload ChatMessage chatMessage,
        SimpMessageHeaderAccessor simpMessageHeaderAccessor
    ) {
        //Add username in web socket session
        // Log existing session attributes
        System.out.println("Existing session attributes: " + simpMessageHeaderAccessor.getSessionAttributes());
        simpMessageHeaderAccessor.getSessionAttributes().put("usernameintchat", chatMessage.getSender());
        // Log updated session attributes
        System.out.println("Updated session attributes: " + simpMessageHeaderAccessor.getSessionAttributes());
        return chatMessage;
    }

    /**
     * handling private chat message
     * @param chatRoomId "userID1_userID2" with userID1 < userID2
     * @param message
     */
    @MessageMapping("/private/{chatRoomId}")
    public void handlePrivateChatMessage(@DestinationVariable String chatRoomId, ChatMessage message) {
        // Logic to handle the message, such as broadcasting it to users subscribed to this chat room
        simpMessagingTemplate.convertAndSend("/topic/private/" + chatRoomId, message);
    }
}
