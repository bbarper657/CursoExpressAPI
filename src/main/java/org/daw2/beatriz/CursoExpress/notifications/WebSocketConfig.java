package org.daw2.beatriz.CursoExpress.notifications;

import io.jsonwebtoken.Claims;
import org.daw2.beatriz.CursoExpress.services.CustomUserDetailsService;
import org.daw2.beatriz.CursoExpress.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic"); // Canales de comunicación
        registry.setUserDestinationPrefix("/user"); // Permite enviar mensajes privados a usuarios específicos
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
                String token = accessor.getFirstNativeHeader("Authorization");

                logger.info("📡 WebSocket intentando autenticar...");

                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                    String username = jwtUtil.extractUsername(token);

                    logger.info("🔑 Token recibido en WebSocket: " + token);
                    logger.info("👤 Usuario extraído del token: " + username);

                    if (jwtUtil.validateToken(token, username)) {
                        var userDetails = userDetailsService.loadUserByUsername(username);
                        Claims claims = jwtUtil.extractAllClaims(token);
                        List<String> roles = claims.get("roles", List.class);

                        logger.info("✅ Usuario autenticado en WebSocket: " + username + " con roles: " + roles);

                        List<SimpleGrantedAuthority> authorities = roles.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList();

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                        // Asocia la autenticación al contexto de seguridad de Spring
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        // Asocia la autenticación al WebSocket
                        accessor.setUser(authToken);
                    } else {
                        logger.warn("❌ Token inválido en WebSocket para el usuario: " + username);
                    }
                } else {
                    logger.warn("⚠️ No se encontró un token en la conexión WebSocket.");
                }

                return message;
            }
        });
    }
}
