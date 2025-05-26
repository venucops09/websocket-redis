package com.websocket.redis.messaging.server.monitor;

import com.websocket.redis.messaging.server.config.ConnectionStateManager;
import com.websocket.redis.messaging.server.registry.UserSessionRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeartbeatMonitor {

    private final ConnectionStateManager connectionStateManager;
    private final UserSessionRegistry userSessionRegistry;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${app.websocket.topic.destination.queue}")
    private String TOPIC_DESTINATION_QUEUE;

    @Scheduled(fixedRate = 15000) // every 15 seconds
    public void checkHeartbeats() {
        Instant now = Instant.now();
        long timeoutSeconds = 60;

        connectionStateManager.getLastHeartbeats().forEach((username, lastSeen) -> {
            if (Duration.between(lastSeen, now).getSeconds() > timeoutSeconds) {
                log.warn("User '{}' timed out due to no heartbeat.", username);

                // Remove user
                connectionStateManager.removeUser(username);
                userSessionRegistry.removeUser(username);

                // Notify others
                messagingTemplate.convertAndSend(TOPIC_DESTINATION_QUEUE, userSessionRegistry.getOnlineUsers());
            }
        });
    }
}
