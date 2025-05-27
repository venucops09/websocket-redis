package com.websocket.redis.messaging.server.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.websocket")
public class WebSocketProperties {
    private String endpoint;
    private List<String> allowedOrigins;
    private boolean sockjsEnabled;
    private List<String> applicationDestinationPrefixes;
    private List<String> brokerDestinationPrefixes;
    private String userDestinationPrefix;
}

