package io.ethertale.findadicethymeleaf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // This turns on the STOMP messaging system
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Think of this like setting up two post boxes:
     *
     * 1. /topic  → the "outbox" — messages FROM the server TO browsers.
     *              The browser subscribes to a topic like a YouTube channel.
     *              When the server posts something there, everyone subscribed gets it.
     *
     * 2. /app    → the "inbox" — messages FROM the browser TO the server.
     *              When a user sends a message, it goes to /app/chat/{roomCode}.
     *              Spring finds a method annotated with @MessageMapping("/chat/{roomCode}") to handle it.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory broker for broadcasting to /topic destinations
        config.enableSimpleBroker("/topic");
        // Any message from the client destined for a @MessageMapping method must start with /app
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * This registers the actual WebSocket "handshake" URL.
     * The browser will connect to: ws://yourserver/ws
     *
     * .withSockJS() is a safety net — if the browser doesn't support WebSockets
     * (very rare these days), SockJS will fall back to polling. It's like
     * having a backup plan that still works, just a bit slower.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }
}