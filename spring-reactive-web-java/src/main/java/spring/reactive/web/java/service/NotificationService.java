package spring.reactive.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import spring.reactive.web.java.domain.NotificationReception;
import spring.reactive.web.java.dto.request.NotificationSaveRequest;
import spring.reactive.web.java.dto.response.NotificationResponse;
import spring.reactive.web.java.repository.NotificationReceptionRepository;
import spring.reactive.web.java.repository.NotificationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
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

    @Transactional
    public Mono<NotificationResponse> saveNotification(
            Long memberId,
            NotificationSaveRequest notificationSaveRequest
    ) {
        return notificationRepository.findById(notificationSaveRequest.notificationId())
                .zipWhen(notification -> notificationReceptionRepository.save(
                        NotificationReception.create(memberId, notification.getNotificationId())
                ))
                .map(TupleUtils.function((notification, notificationReception) ->
                        new NotificationResponse(notification, notificationReception)));
    }
}
