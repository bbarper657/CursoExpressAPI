package org.daw2.beatriz.CursoExpress.services;

import org.daw2.beatriz.CursoExpress.dtos.NotificationCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.NotificationDTO;
import org.daw2.beatriz.CursoExpress.entities.Notification;
import org.daw2.beatriz.CursoExpress.mappers.NotificationMapper;
import org.daw2.beatriz.CursoExpress.repositories.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public Mono<NotificationDTO> saveNotification(NotificationCreateDTO notificationCreateDTO) {
        Notification notification = NotificationMapper.toEntity(notificationCreateDTO);
        return notificationRepository.save(notification)
                // Se ejecuta cuando la notificación ha sido guardada con éxito
                .doOnSuccess(saveNotification ->
                        // Crea un Mono para ejecutar ola tarea de enviar la notificación por WebSocket
                        Mono.fromRunnable(() -> messagingTemplate.convertAndSend(
                                        "/topic/notifications",
                                        NotificationMapper.toDTO(saveNotification) // Convertimos la entidad guardada a DTO
                                ))
                                .subscribeOn(Schedulers.boundedElastic()) // Se ejecuta en un pool de hilos elástico para evitar bloqueos
                                .subscribe() // Suscripción para ejecutar la tarea asíncrona e envío
                )
                .map(NotificationMapper::toDTO); // Convierte la notificación guardada en DTO antes de devolverla
    }

    public Flux<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().map(NotificationMapper::toDTO);
    }
}
