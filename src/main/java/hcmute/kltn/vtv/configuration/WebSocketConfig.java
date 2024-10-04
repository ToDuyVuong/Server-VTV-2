package hcmute.kltn.vtv.configuration;

import hcmute.kltn.vtv.authentication.service.IJwtService;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final IJwtService jwtService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/room");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/room");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setHandshakeHandler(new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(org.springframework.http.server.ServerHttpRequest request, WebSocketHandler wsHandler,
                    Map<String, Object> attributes) {
                String Authorization =  request.getURI().getQuery().split("=")[1];

                if (Authorization == null || Authorization.trim().isEmpty()) {
                    throw new BadRequestException("Thiếu thông tin người gửi");
                }
                String username = jwtService.extractUsername(Authorization);
                if (username == null || username.isEmpty()) {
                    throw new BadRequestException("Không tìm thấy thông tin người gửi");
                }

                return () -> Authorization;
            }
        }).setAllowedOrigins("*");
    }
}
