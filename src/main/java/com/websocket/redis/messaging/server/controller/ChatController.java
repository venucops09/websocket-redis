package com.websocket.redis.messaging.server.controller;

import com.websocket.redis.messaging.server.dto.ChatMessage;
import com.websocket.redis.messaging.server.registry.UserSessionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
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

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserSessionRegistry userSessionRegistry;

    private static final String SEND_MESSAGE = "/send-message";
    private static final String REQUEST_ONLINE_USERS = "/request-online-users";

    @Value("${redis.pubsub.channel}")
    private String redisChannel;

    @Value("${app.websocket.user.destination.queue}")
    private String USER_DESTINATION_QUEUE;


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
    @MessageMapping(SEND_MESSAGE)      // Clients send to: /app/send-message
    public void sendMessage(ChatMessage message, Principal principal) {
        message.setFrom(principal.getName());
        redisTemplate.convertAndSend(redisChannel, message);
    }

    /**
     * Handles a request from the client to fetch the current list of online users.
     * <p>
     * When a client sends a message to the {@code REQUEST_ONLINE_USERS} destination,
     * this method sends the updated list of online users specifically to the requesting user.
     * </p>
     *
     * @param principal the authenticated user making the request; used to send the response to the correct user
     */
    @MessageMapping(REQUEST_ONLINE_USERS)
    public void handleOnlineUserRequest(Principal principal) {
        //Send updated list to current logged in user
        if (principal != null) {
            messagingTemplate.convertAndSendToUser(
                    principal.getName(),
                    USER_DESTINATION_QUEUE,
                    userSessionRegistry.getOnlineUsers()
            );
        }
    }

}
