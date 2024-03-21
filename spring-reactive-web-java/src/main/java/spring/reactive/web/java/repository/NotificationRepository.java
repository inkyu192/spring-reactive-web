package spring.reactive.web.java.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import spring.reactive.web.java.domain.Notification;

import java.util.List;

public interface NotificationRepository extends R2dbcRepository<Notification, Long> {

    Flux<Notification> findByIdIn(List<Long> notificationIds);
}
