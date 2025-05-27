package com.websocket.redis.messaging.server.config;

import com.websocket.redis.messaging.server.dto.ChatMessage;
import com.websocket.redis.messaging.server.dto.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        log.info("Received a new web socket connection");
    }

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//
//        if (username != null) {
//            log.info("user disconnected: {}", username);
//
//            var chatMessage = ChatMessage.builder()
//                    .type(MessageType.LEAVE)
//                    .sender(username)
//                    .build();
//
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }
}
