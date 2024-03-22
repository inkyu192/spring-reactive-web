package spring.reactive.web.java.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import spring.reactive.web.java.domain.Notification;

public interface NotificationRepository extends R2dbcRepository<Notification, Long> {
}
