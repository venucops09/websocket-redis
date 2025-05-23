package com.websocket.redis.messaging.server.subscriber;

import com.websocket.redis.messaging.server.registry.UserSessionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final UserSessionRegistry userSessionRegistry;

    @Value("${app.websocket.topic.destination.queue}")
    private String TOPIC_DESTINATION_QUEUE;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            userSessionRegistry.addUser(user.getName());
            // Send updated list to all users
            messagingTemplate.convertAndSend(TOPIC_DESTINATION_QUEUE, userSessionRegistry.getOnlineUsers());
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            userSessionRegistry.removeUser(user.getName());
            messagingTemplate.convertAndSend(TOPIC_DESTINATION_QUEUE, userSessionRegistry.getOnlineUsers());
        }
    }
}

