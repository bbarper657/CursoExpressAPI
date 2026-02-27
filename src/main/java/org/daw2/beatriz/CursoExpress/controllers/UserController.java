package org.daw2.beatriz.CursoExpress.controllers;

import org.daw2.beatriz.CursoExpress.dtos.UserDTO;
import org.daw2.beatriz.CursoExpress.services.UserService;
import org.daw2.beatriz.CursoExpress.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String tokenHeader) {
        logger.info("Solicitando la información del usuario solicitado");

        // Limpiamos el prefijo "Bearer"
        String token = tokenHeader.replace("Bearer ", "");

        // Usamos el servicio JWT/Utilidad para extraer el id
        Long id = jwtUtil.extractClaim(token, claims -> claims.get("id", Long.class));

        // Opcion 2
        // String token = request.getRequestHeaders().get("Authorization").subList(7, request.getRequestHeaders().get("Authorization").size()).toString();
        try {
            UserDTO userDTO = userService.getUser(id);
            logger.info("Se han encontrado el usuario con ID {}", id);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            logger.error("Error al buscar el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar el usuario.");
        }
    }
}
