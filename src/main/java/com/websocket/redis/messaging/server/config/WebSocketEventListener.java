package com.websocket.redis.messaging.server.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            userSessionRegistry.addUser(user.getName());
            // Send updated list to all users
            messagingTemplate.convertAndSend("/topic/online-users", userSessionRegistry.getOnlineUsers());
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user != null) {
            userSessionRegistry.removeUser(user.getName());
            messagingTemplate.convertAndSend("/topic/online-users", userSessionRegistry.getOnlineUsers());
        }
    }
}

