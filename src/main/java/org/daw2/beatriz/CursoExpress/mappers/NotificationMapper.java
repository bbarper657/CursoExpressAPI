package org.daw2.beatriz.CursoExpress.mappers;

import org.daw2.beatriz.CursoExpress.dtos.NotificationCreateDTO;
import org.daw2.beatriz.CursoExpress.dtos.NotificationDTO;
import org.daw2.beatriz.CursoExpress.entities.Notification;

import java.time.Instant;

public class NotificationMapper {
    public static NotificationDTO toDTO(Notification notification) {
        if (notification == null) {
            return null;
        }
        return new NotificationDTO(
                notification.getId(),
                notification.getSubject(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }

    public static Notification toEntity(NotificationCreateDTO notificationCreateDTO) {
        if (notificationCreateDTO == null) {
            return null;
        }
        return new Notification(
                null,
                notificationCreateDTO.getSubject(),
                notificationCreateDTO.getMessage(),
                notificationCreateDTO.isRead(),
                Instant.now()
        );
    }
}
