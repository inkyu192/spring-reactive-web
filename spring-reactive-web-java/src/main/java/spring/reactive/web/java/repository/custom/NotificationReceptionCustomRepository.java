package spring.reactive.web.java.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import spring.reactive.web.java.domain.Notification;
import spring.reactive.web.java.domain.NotificationReception;

public interface NotificationReceptionCustomRepository {

    Mono<Page<Tuple2<Notification, NotificationReception>>> findByMemberId(Pageable pageable, Long memberId);
}
