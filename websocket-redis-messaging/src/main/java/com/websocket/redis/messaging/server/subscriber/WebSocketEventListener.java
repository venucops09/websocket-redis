package com.websocket.redis.messaging.server.subscriber;

import com.websocket.redis.messaging.server.config.ConnectionStateManager;
import com.websocket.redis.messaging.server.registry.UserSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final UserSessionRegistry userSessionRegistry;

    @Value("${app.websocket.topic.destination.queue}")
    private String TOPIC_DESTINATION_QUEUE;

    @Autowired
    ConnectionStateManager connectionStateManager;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        try {
            if (event == null) {
                log.warn("SessionConnectedEvent is null.");
                return;
            }

            Principal user = event.getUser();
            if (user == null || user.getName() == null) {
                log.warn("Connected user is null or username is missing.");
                return;
            }

            String userName = user.getName();
            connectionStateManager.onConnect(userName);

            userSessionRegistry.addUser(userName);
            messagingTemplate.convertAndSend(TOPIC_DESTINATION_QUEUE, userSessionRegistry.getOnlineUsers());

            log.info("'{}' connected. Online users updated.", userName);

        } catch (Exception e) {
            log.error("Error handling WebSocket connect event: {}", e.getMessage(), e);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        try {
            if (event == null) {
                log.warn("SessionDisconnectEvent is null.");
                return;
            }

            Principal user = event.getUser();
            if (user == null || user.getName() == null) {
                log.warn("Disconnected user is null or username is missing.");
                return;
            }

            String userName = user.getName();
            userSessionRegistry.removeUser(userName);
            messagingTemplate.convertAndSend(TOPIC_DESTINATION_QUEUE, userSessionRegistry.getOnlineUsers());
            connectionStateManager.onDisconnect(userName);

            log.info("'{}' disconnected. Online users updated.", userName);

        } catch (Exception e) {
            log.error("Error handling WebSocket disconnect event: {}", e.getMessage(), e);
        }
    }
}

