package com.websocket.redis.messaging.server.config;

import com.websocket.redis.messaging.server.dto.ConnectionState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class ConnectionStateManager {

    private final AtomicReference<ConnectionState> state = new AtomicReference<>(ConnectionState.DISCONNECTED);
    private final Map<String, Instant> lastHeartbeatMap = new ConcurrentHashMap<>();

    // Called when a user establishes a connection
    public void onConnect(String userName) {
        state.set(ConnectionState.CONNECTED);
        lastHeartbeatMap.put(userName, Instant.now());
        log.info("Connection established. State: '{}' CONNECTED", userName);
    }

    // Called when a user disconnects
    public void onDisconnect(String userName) {
        state.set(ConnectionState.DISCONNECTED);
        log.warn("Connection lost. State: '{}' DISCONNECTED", userName);
    }

    // Called when heartbeat is received from a specific user
    public void onHeartbeatReceived(String userName) {
        lastHeartbeatMap.put(userName, Instant.now());
        log.info("Heartbeat received from {} at {}", userName, Instant.now());
    }

    public Map<String, Instant> getLastHeartbeats() {
        return lastHeartbeatMap;
    }

    public void removeUser(String username) {
        lastHeartbeatMap.remove(username);
    }
}
