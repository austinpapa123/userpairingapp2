package com.chuyue.usercenter.config;

import com.chuyue.usercenter.common.MessageType;
import com.chuyue.usercenter.model.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void webSocketDisconnectionListener(
            SessionDisconnectEvent event
    ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        //String usernameinchat = (String) headerAccessor.getSessionAttributes().get("usernameinchat");
//        if(usernameinchat != null) {
//            log.info("User {} disconnected", usernameinchat);
//            var chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE)
//                    .sender(usernameinchat).build();
//            messageTemplate.convertAndSend("/topic/testing", chatMessage);
//        }

        System.out.println(headerAccessor.getMessage());
        var chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE)
                    .sender("someone").build();
        messageTemplate.convertAndSend("/topic/testing", chatMessage);
    }
}
