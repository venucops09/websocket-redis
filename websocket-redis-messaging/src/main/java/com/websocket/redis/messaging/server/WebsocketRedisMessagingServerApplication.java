package com.websocket.redis.messaging.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WebsocketRedisMessagingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsocketRedisMessagingServerApplication.class, args);
	}

}
