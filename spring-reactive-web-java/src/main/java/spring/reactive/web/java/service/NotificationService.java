package spring.reactive.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import spring.reactive.web.java.dto.response.NotificationResponse;
import spring.reactive.web.java.repository.NotificationReceptionRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationReceptionRepository notificationReceptionRepository;

    public Mono<Page<NotificationResponse>> findNotifications(Pageable pageable, Long memberId) {
        return notificationReceptionRepository.findByMemberId(pageable, memberId)
                .map(tuple2s ->
                        tuple2s.map(TupleUtils.function((notification, notificationReception) ->
                                new NotificationResponse(
                                        notification,
                                        notificationReception
                                )
                        ))
                );
    }
}
