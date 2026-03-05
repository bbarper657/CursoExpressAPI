package org.daw2.beatriz.CursoExpress.notifications;

import org.daw2.beatriz.CursoExpress.controllers.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSocketSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain websocketSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/ws/**") // Aplicar esta configuración solo a las rutas que empiezan con /ws/
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permitir acceso sin autenticación a los WebSockets
                )
                .csrf(csrf -> csrf.disable()) // Deshabilitar la protección CSRF ya que WebSockets no la requieren
                // Permitir WebSockets en navegadores sin restricciones
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                // Aplicar el filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
