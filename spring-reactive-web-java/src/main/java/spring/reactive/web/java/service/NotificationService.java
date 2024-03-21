package spring.reactive.web.java.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.util.function.Tuples;
import spring.reactive.web.java.domain.Notification;
import spring.reactive.web.java.domain.NotificationReception;
import spring.reactive.web.java.dto.response.NotificationResponse;
import spring.reactive.web.java.repository.NotificationReceptionRepository;
import spring.reactive.web.java.repository.NotificationRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationReceptionRepository notificationReceptionRepository;

    public Flux<NotificationResponse> findNotifications(Long memberId) {
        return notificationReceptionRepository.findByMemberId(memberId)
                .collectList()
                .zipWhen(notificationReceptions -> {
                    List<Long> notificationIds = notificationReceptions.stream()
                            .map(NotificationReception::getNotificationId)
                            .toList();

                    return notificationRepository.findByIdIn(notificationIds).collectList();
                }, (notificationReceptions, notifications) -> {
                    Map<Long, Notification> notificationMap = notifications.stream()
                            .collect(Collectors.toMap(
                                    Notification::getNotificationId,
                                    notification -> notification
                            ));

                    return Tuples.of(notificationReceptions, notificationMap);
                })
                .flatMapMany(TupleUtils.function((notificationReceptions, notificationMap) ->
                        Flux.fromStream(
                                notificationReceptions.stream()
                                        .map(notificationReception -> new NotificationResponse(
                                                notificationMap.get(notificationReception.getNotificationId()),
                                                notificationReception
                                        ))
                        )
                ));
    }

    public Mono<Page<NotificationResponse>> findNotifications(Pageable pageable, Long memberId) {
        return notificationReceptionRepository.findWithR2dbcEntityOperations(pageable, memberId)
                .zipWhen(notificationReceptionPage -> {
                    List<Long> notificationIds = notificationReceptionPage.stream()
                            .map(NotificationReception::getNotificationId)
                            .toList();

                    return notificationRepository.findByIdIn(notificationIds).collectList();
                }, (notificationReceptionPage, notifications) -> {
                    Map<Long, Notification> notificationMap = notifications.stream()
                            .collect(Collectors.toMap(
                                    Notification::getNotificationId,
                                    notification -> notification
                            ));

                    return Tuples.of(notificationReceptionPage, notificationMap);
                })
                .map(TupleUtils.function((notificationReceptionPage, notificationMap) -> notificationReceptionPage
                        .map(notificationReception -> new NotificationResponse(
                                notificationMap.get(notificationReception.getNotificationId()),
                                notificationReception
                        ))
                ));
    }
}
