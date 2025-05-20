package com.websocket.redis.messaging.server.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.redis.messaging.server.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Subscriber for Redis Pub/Sub messages.
 * Listens for messages published on Redis channels and handles them accordingly.
 */
@Component
@Slf4j
public class RedisMessageSubscriber implements MessageListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Message received from redis channel");
            String msgBody = new String(message.getBody());
            ChatMessage chatMessage = objectMapper.readValue(msgBody, ChatMessage.class);
            // Deliver to WebSocket subscribers
            messagingTemplate.convertAndSendToUser(chatMessage.getTo(), "/queue/messages", chatMessage);
            log.info("Message send to {}", chatMessage.getTo());
        } catch (Exception e) {
            log.error("Error while sending message to user {}", e.getMessage());
        }
    }
}