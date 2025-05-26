package com.websocket.redis.messaging.server.dto;

/**
 * Represents the connection state of a WebSocket client.
 * This enum is used to track whether a client is currently connected
 * or has been disconnected from the WebSocket server.
 */
public enum ConnectionState {
    /**
     * Indicates that the client is currently connected to the WebSocket server.
     */
    CONNECTED,
    
    /**
     * Indicates that the client has been disconnected from the WebSocket server.
     */
    DISCONNECTED
}

