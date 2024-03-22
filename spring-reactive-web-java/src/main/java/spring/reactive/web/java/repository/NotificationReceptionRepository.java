package spring.reactive.web.java.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import spring.reactive.web.java.domain.NotificationReception;
import spring.reactive.web.java.repository.custom.NotificationReceptionCustomRepository;

public interface NotificationReceptionRepository extends R2dbcRepository<NotificationReception, Long>, NotificationReceptionCustomRepository {
}
