package spring.reactive.web.java.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.domain.NotificationReception;

public interface NotificationReceptionCustomRepository {

    Mono<Page<NotificationReception>> findWithR2dbcEntityOperations(Pageable pageable, Long memberId);
}
