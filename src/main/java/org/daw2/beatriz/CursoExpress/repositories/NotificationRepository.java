package org.daw2.beatriz.CursoExpress.repositories;

import org.daw2.beatriz.CursoExpress.entities.Notification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationRepository extends ReactiveMongoRepository<Notification,String> {
}
