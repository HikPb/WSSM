package com.project.wsms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.project.wsms.payload.request.MessageRequest;
import com.project.wsms.security.OnlineUsers;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);
    
    private final OnlineUsers onlineUsers;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        // if(!this.onlineUsers.getActiveSessions().values().contains(username)){

        // }
        this.onlineUsers.add(headers.getSessionId(), username);
        logger.info("User connnected: {}", username);
        var message = MessageRequest.builder()
                .type("CONNECT")
                .sender(username)
                .message(username + " connected")
                .data(this.onlineUsers.getActiveSessions().values())
                .build();
        messagingTemplate.convertAndSend("/all", message, null, null);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // StompHeaderAccessor headerAccessor =
        // StompHeaderAccessor.wrap(event.getMessage());
        // String username = (String)
        // headerAccessor.getSessionAttributes().get("username");
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        if (username != null) {
            logger.info("user disconnected: {}", username);
            this.onlineUsers.removeParticipant(event.getSessionId());
            var message = MessageRequest.builder()
                    .type("DISCONNECT")
                    .sender(username)
                    .message("User disconnected")
                    .data(this.onlineUsers.getActiveSessions().values())
                    .build();
            messagingTemplate.convertAndSend("/all", message, null, null);
        }
    }
}
