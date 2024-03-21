package spring.reactive.web.java.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import spring.reactive.web.java.domain.NotificationReception;
import spring.reactive.web.java.repository.custom.NotificationReceptionCustomRepository;

import java.util.List;

public interface NotificationReceptionRepository extends R2dbcRepository<NotificationReception, Long>, NotificationReceptionCustomRepository {

    Flux<NotificationReception> findByMemberId(Long memberId);
}
