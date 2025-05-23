package com.websocket.redis.messaging.server.config;

import com.websocket.redis.messaging.server.dto.WebSocketProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration class that enables and configures message broker for STOMP messaging.
 * Registers endpoint for WebSocket connection and sets up application destination prefixes and broker relay.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketProperties properties;

    public WebSocketConfig(WebSocketProperties properties) {
        this.properties = properties;
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        var endpointRegistration = registry.addEndpoint(properties.getEndpoint());

        if (properties.getAllowedOrigins() != null && !properties.getAllowedOrigins().isEmpty()) {
            String[] origins = properties.getAllowedOrigins().toArray(new String[0]);
            endpointRegistration.setAllowedOriginPatterns(origins);
        }

        if (properties.isSockjsEnabled()) {
            endpointRegistration.withSockJS();
        }
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        if (properties.getApplicationDestinationPrefixes() != null) {
            registry.setApplicationDestinationPrefixes(properties.getApplicationDestinationPrefixes().toArray(new String[0]));
        }
        if (properties.getBrokerDestinationPrefixes() != null) {
            registry.enableSimpleBroker(properties.getBrokerDestinationPrefixes().toArray(new String[0]));
        }
        if (properties.getUserDestinationPrefix() != null) {
            registry.setUserDestinationPrefix(properties.getUserDestinationPrefix());
        }
    }
}