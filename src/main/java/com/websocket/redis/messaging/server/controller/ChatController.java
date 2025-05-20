package com.websocket.redis.messaging.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.redis.messaging.server.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * Controller responsible for handling WebSocket chat messages sent by clients.
 * This controller listens for messages on the "/app/send-message" destination,
 * sets the sender based on the authenticated user, and publishes the message
 * to a Redis channel for asynchronous delivery to other connected clients.
 */
@Controller
@Slf4j
public class ChatController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Handles incoming chat messages sent by users over WebSocket.
     *
     * <p>Clients send messages to the "/app/send-message" destination.
     * The sender (from) field of the message is automatically set to
     * the authenticated user's name. The message is then published to
     * the "chat" Redis channel, where it can be processed by subscribers
     * for delivery to recipients.</p>
     *
     * @param message   the chat message received from the client
     * @param principal the currently authenticated user
     */
    @MessageMapping("/send-message")      // Clients send to: /app/send-message
    public void sendMessage(ChatMessage message, Principal principal) {
        log.info("Received in controller send-message");
        message.setFrom(principal.getName());
        redisTemplate.convertAndSend("chat", message);
    }
}
