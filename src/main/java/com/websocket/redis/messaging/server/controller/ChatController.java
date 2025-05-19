package com.websocket.redis.messaging.server.controller;

import com.websocket.redis.messaging.server.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @MessageMapping("/send-message")      // Clients send to: /app/send-message
    @SendTo("/topic/messages")            // Clients subscribe to: /topic/messages
    public String handleMessage(ChatMessage message) {
        String redisChannel = "chat:" + message.getTo(); // e.g., chat:userB
        redisTemplate.convertAndSend(redisChannel, message);
        return "Server received: " + message;
    }
}
