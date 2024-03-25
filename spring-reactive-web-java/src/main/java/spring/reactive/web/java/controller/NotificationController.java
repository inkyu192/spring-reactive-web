package spring.reactive.web.java.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import spring.reactive.web.java.config.security.UserDetailsImpl;
import spring.reactive.web.java.dto.request.NotificationSaveRequest;
import spring.reactive.web.java.dto.response.ApiResponse;
import spring.reactive.web.java.dto.response.NotificationResponse;
import spring.reactive.web.java.service.NotificationService;

@RestController
@RequestMapping("notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public Mono<ApiResponse<Page<NotificationResponse>>> findNotifications(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) Long memberId
    ) {
        return notificationService.findNotifications(pageable, memberId)
                .map(ApiResponse::new);
    }

    @PostMapping
    public Mono<ApiResponse<NotificationResponse>> saveNotification(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            NotificationSaveRequest notificationSaveRequest
    ) {
        return notificationService.saveNotification(userDetails.getMemberId(), notificationSaveRequest)
                .map(ApiResponse::new);
    }
}
