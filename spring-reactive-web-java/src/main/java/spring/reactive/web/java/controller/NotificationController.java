package spring.reactive.web.java.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import spring.reactive.web.java.config.security.UserDetailsImpl;
import spring.reactive.web.java.dto.request.NotificationSaveRequest;
import spring.reactive.web.java.dto.response.ApiResponse;
import spring.reactive.web.java.dto.response.NotificationResponse;
import spring.reactive.web.java.service.NotificationService;

import java.util.Objects;

@RestController
@RequestMapping("notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final Sinks.Many<NotificationResponse> sink;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
        sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @GetMapping
    public Mono<ApiResponse<Page<NotificationResponse>>> findNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return notificationService.findNotifications(pageable, userDetails.getMemberId())
                .map(ApiResponse::new);
    }

    @GetMapping("sse")
    public Flux<ServerSentEvent<NotificationResponse>> findNotificationSSE(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return sink.asFlux()
                .filter(notificationResponse -> Objects.equals(notificationResponse.memberId(), userDetails.getMemberId()))
                .map(notificationResponse -> ServerSentEvent.<NotificationResponse>builder().build())
                .doOnCancel(() -> sink.asFlux().blockLast());
    }

    @PostMapping
    public Mono<ApiResponse<NotificationResponse>> saveNotification(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            NotificationSaveRequest notificationSaveRequest
    ) {
        return notificationService.saveNotification(userDetails.getMemberId(), notificationSaveRequest)
                .doOnNext(sink::tryEmitNext)
                .map(ApiResponse::new);
    }
}
