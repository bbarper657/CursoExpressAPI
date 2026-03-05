package org.daw2.beatriz.CursoExpress.controllers;

import org.daw2.beatriz.CursoExpress.dtos.NotificationCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.NotificationDTO;
import org.daw2.beatriz.CursoExpress.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ws/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public Flux<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @PostMapping
    public Mono<NotificationDTO> createNotification(@RequestBody NotificationCreateDTO notificationCreateDTO) {
        return notificationService.saveNotification(notificationCreateDTO);
    }
}
