package com.websocket.redis.messaging.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing a chat message exchanged between users.
 *
 * <p>This class encapsulates the sender, recipient, and message content.
 * It is used for communication between clients over WebSocket and is also
 * published to and consumed from Redis channels for message distribution.</p>
 *
 * */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage implements Serializable {
    private String from;
    private String to;
    private String content;
}